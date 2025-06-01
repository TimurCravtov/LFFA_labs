package md.utm.parser;

import md.utm.tokenizer.Token;
import md.utm.tokenizer.TokenType;
import md.utm.parser.astnodes.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AionParser {
    private final Iterator<Token> tokens;
    private Token currentToken;

    public AionParser(List<Token> tokens) {
        this.tokens = tokens.iterator();
        advance();
    }

    private void advance() {
        currentToken = tokens.hasNext() ? tokens.next() : null;
    }

    private void expect(TokenType type) {
        if (currentToken == null || currentToken.getType() != type) {
            throw new RuntimeException("Expected " + type + " but got " + (currentToken != null ? currentToken.getType() : "EOF"));
        }
        advance();
    }

    private void expectValue(String value) {
        if (currentToken == null || !value.equals(currentToken.getValue())) {
            throw new RuntimeException("Expected '" + value + "' but got " + (currentToken != null ? currentToken.getValue() : "EOF"));
        }
        advance();
    }

    public ProgramNode parse() {
        List<StatementNode> statements = new ArrayList<>();
        Position startPos = currentToken != null ? currentToken.getPosition() : new Position(0, 0);

        while (currentToken != null) {
            statements.add(parseStatement());
        }

        return new ProgramNode(startPos, statements);
    }

    private StatementNode parseStatement() {
        if (currentToken == null) {
            throw new RuntimeException("Unexpected end of input");
        }

        StatementNode stmt;
        String assignmentVariable = null;

        // Check for assignment
        if (currentToken.getType() == TokenType.VARIABLE) {
            assignmentVariable = currentToken.getValue();
            advance();
            if (currentToken != null && currentToken.getValue().equals("=")) {
                advance();
            }
        }

        // Parse different statement types
        if (currentToken.getType() == TokenType.KEYWORD) {
            switch (currentToken.getValue()) {
                case "import":
                    stmt = parseImport();
                    break;
                case "new":
                    advance();
                    if (currentToken.getValue().equals("task")) {
                        stmt = parseTask(assignmentVariable);
                    } else if (currentToken.getValue().equals("event")) {
                        stmt = parseEvent(assignmentVariable);
                    } else {
                        throw new RuntimeException("Expected 'task' or 'event' after 'new'");
                    }
                    break;
                case "filter":
                    stmt = parseFilter();
                    break;
                case "export":
                    stmt = parseExport();
                    break;
                case "pomodoro":
                    stmt = parsePomodoro(assignmentVariable);
                    break;
                case "merge":
                    stmt = parseMerge();
                    break;
                default:
                    throw new RuntimeException("Unexpected keyword: " + currentToken.getValue());
            }
        } else {
            throw new RuntimeException("Unexpected token: " + currentToken.getValue());
        }

        // Expect semicolon at the end of statement
        if (currentToken != null && currentToken.getValue().equals(";")) {
            advance();
        }

        return stmt;
    }

    private ImportStatementNode parseImport() {
        Position pos = currentToken.getPosition();
        advance(); // consume 'import'

        // Expect string literal (path)
        String path = currentToken.getValue();
        advance();

        // Expect 'as'
        expectValue("as");

        // Expect alias
        String alias = currentToken.getValue();
        advance();

        return new ImportStatementNode(pos, path, alias);
    }

    private TaskNode parseTask(String assignmentVariable) {
        Position pos = currentToken.getPosition();
        advance(); // consume 'task'

        // Expect task name (string)
        String name = currentToken.getValue();
        advance();

        // Parse repeat condition if present
        String repeatCondition = null;
        if (currentToken.getType() == TokenType.REPEAT_CONDITION) {
            repeatCondition = currentToken.getValue();
            advance();
        }

        // Expect 'at'
        expectValue("at");

        // Expect time
        String time = currentToken.getValue();
        advance();

        return new TaskNode(pos, name, time, repeatCondition, assignmentVariable);
    }

    private EventNode parseEvent(String assignmentVariable) {
        Position pos = currentToken.getPosition();
        advance(); // consume 'event'

        // Expect event name (string)
        String name = currentToken.getValue();
        advance();

        String date = null;
        String dayOfWeek = null;
        String time = null;
        String duration = null;

        // Parse optional date/time specifications
        while (currentToken != null && !currentToken.getValue().equals(";")) {
            switch (currentToken.getValue()) {
                case "on":
                    advance();
                    if (currentToken.getType() == TokenType.DATE) {
                        date = currentToken.getValue();
                    } else if (currentToken.getType() == TokenType.DAY_OF_WEEK) {
                        dayOfWeek = currentToken.getValue();
                    }
                    advance();
                    break;
                case "at":
                    advance();
                    time = currentToken.getValue();
                    advance();
                    break;
                case "for":
                    advance();
                    duration = currentToken.getValue();
                    advance();
                    break;
                default:
                    throw new RuntimeException("Unexpected token in event: " + currentToken.getValue());
            }
        }

        return new EventNode(pos, name, time, duration, date, dayOfWeek, assignmentVariable);
    }

    private FilterNode parseFilter() {
        Position pos = currentToken.getPosition();
        advance(); // consume 'filter'

        // Parse source calendar
        String sourceCalendar = currentToken.getValue();
        advance();

        // Expect 'where'
        expectValue("where");

        // Parse field
        String field = currentToken.getValue();
        advance();

        // Parse operator
        String operator = currentToken.getValue();
        advance();

        // Parse value
        String value = currentToken.getValue();
        advance();

        // Expect 'into'
        expectValue("into");

        // Parse target variable
        String targetVariable = currentToken.getValue();
        advance();

        return new FilterNode(pos, sourceCalendar, field, operator, value, targetVariable);
    }

    private ExportNode parseExport() {
        Position pos = currentToken.getPosition();
        advance(); // consume 'export'

        // Parse calendar variable
        String calendarVariable = currentToken.getValue();
        advance();

        String exportName = null;
        // Check if there's an export name
        if (currentToken != null && currentToken.getValue().equals("as")) {
            advance();
            exportName = currentToken.getValue();
            advance();
        }

        return new ExportNode(pos, calendarVariable, exportName);
    }

    private PomodoroNode parsePomodoro(String assignmentVariable) {
        Position pos = currentToken.getPosition();
        advance(); // consume 'pomodoro'

        // Expect pomodoro name (string)
        String name = currentToken.getValue();
        advance();

        // Expect 'at'
        expectValue("at");

        // Expect time
        String time = currentToken.getValue();
        advance();

        // Expect 'repeat'
        expectValue("repeat");

        // Expect number of repetitions
        int repetitions = Integer.parseInt(currentToken.getValue());
        advance();

        // Expect 'times'
        expectValue("times");

        return new PomodoroNode(pos, name, time, repetitions, assignmentVariable);
    }

    private MergeNode parseMerge() {
        Position pos = currentToken.getPosition();
        advance(); // consume 'merge'

        List<String> sourceCalendars = new ArrayList<>();
        
        // Parse first calendar
        sourceCalendars.add(currentToken.getValue());
        advance();

        // Parse additional calendars separated by commas
        while (currentToken != null && currentToken.getValue().equals(",")) {
            advance(); // consume comma
            sourceCalendars.add(currentToken.getValue());
            advance();
        }

        // Expect 'into'
        expectValue("into");

        // Parse target variable
        String targetVariable = currentToken.getValue();
        advance();

        return new MergeNode(pos, sourceCalendars, targetVariable);
    }
}