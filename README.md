Sure. Here is an example of how Kotlin can be used to write concise and safe code:

```kotlin
fun sum(numbers: List<Int>): Int {
    return numbers.sum()
}
```

This function takes a list of integers and returns the sum of those integers. The `sum()` function is a built-in function in Kotlin that calculates the sum of a list of numbers.

Here is an example of how Kotlin can be used to prevent errors caused by null values:

```kotlin
fun divide(dividend: Int, divisor: Int): Int? {
    if (divisor == 0) {
        return null
    }
    return dividend / divisor
}
```

This function takes two integers and returns the result of dividing the first integer by the second integer. The function uses the `if` statement to check if the divisor is equal to 0. If the divisor is equal to 0, the function returns `null` to indicate that the division cannot be performed. Otherwise, the function returns the result of the division.

Here is an example of how Kotlin can be used to improve productivity by reducing the amount of code that needs to be written:

```kotlin
fun greet(name: String) = "Hello, $name!"
```

This function takes a string and returns a greeting message. The function uses the `=` operator to define the function body. This is a concise way to write a function in Kotlin.

Overall, Kotlin is a powerful and expressive language that can be used to write concise, safe, and productive code.

Here are some additional examples of how Kotlin can be used to improve productivity and reduce errors:

* Kotlin's null safety feature can help to prevent errors caused by null values. For example, the following code would cause an error in Java, but it is safe in Kotlin:

```kotlin
val name: String? = null
println(name.length) // Error in Java, but safe in Kotlin
```

* Kotlin's concise syntax can help to reduce the amount of code that needs to be written. For example, the following code would be much longer in Java:

```kotlin
val numbers = listOf(1, 2, 3, 4, 5)
val sum = numbers.sum()
println(sum)
```

* Kotlin's interoperability with Java can make it easy to integrate Kotlin into existing Java projects. For example, the following code shows how to call a Java method from Kotlin:

```kotlin
val javaClass = JavaClass()
val result = javaClass.someMethod()
println(result)
```

Overall, Kotlin is a powerful and expressive language that can be used to write concise, safe, and productive code. It is a great choice for developing Android applications, web applications, and desktop applications.