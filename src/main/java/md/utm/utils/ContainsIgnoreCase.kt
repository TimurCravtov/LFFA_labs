package md.utm.utils

fun Set<String>.containsIgnoreCase(element: String): Boolean {
    return this.any { it.equals(element, ignoreCase = true) }
}
