package md.utm.lab2;

public enum ChomskyType {
    TYPE0("Type-0"),
    TYPE1("Type-1"),
    TYPE2("Type-2"),
    TYPE3("Type-3");

    final String name;

    ChomskyType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
