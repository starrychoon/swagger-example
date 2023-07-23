package org.example.swagger.support.restdocs.apispec

import com.epages.restdocs.apispec.FieldDescriptors
import org.springframework.restdocs.constraints.Constraint
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.applyPathPrefix
import org.springframework.restdocs.snippet.AbstractDescriptor
import org.springframework.restdocs.snippet.Attributes.key

/**
 * @author starrychoon
 */
inline fun <reified E : Enum<E>, T : AbstractDescriptor<T>> AbstractDescriptor<T>.enumType(): T {
    return enumValues(enumValues<E>().map { it.name })
}

fun <T : AbstractDescriptor<T>> AbstractDescriptor<T>.enumValues(enumValues: List<Any>): T {
    return attributes(key(Attributes.ENUM_VALUES_KEY).value(enumValues))
}

inline fun <reified E : Enum<E>> FieldDescriptor.enumType(): FieldDescriptor {
    return enumType(enumValues<E>().map { it.name })
}

fun FieldDescriptor.enumType(enumValues: List<Any>): FieldDescriptor {
    return type(Attributes.ENUM_TYPE).attributes(key(Attributes.ENUM_VALUES_KEY).value(enumValues))
}

/**
 * Constrain for array type
 */
@Suppress("UNCHECKED_CAST")
fun <T : AbstractDescriptor<T>> T.size(min: Int? = null, max: Int? = null): T {
    val constraints =
        attributes.computeIfAbsent(Attributes.CONSTRAINTS_KEY) { mutableListOf<Constraint>() } as MutableList<Constraint>
    constraints += SizeConstraint(min, max)
    return this
}

/**
 * Constrain for string type
 */
@Suppress("UNCHECKED_CAST")
fun <T : AbstractDescriptor<T>> T.length(min: Int? = null, max: Int? = null): T {
    val constraints =
        attributes.computeIfAbsent(Attributes.CONSTRAINTS_KEY) { mutableListOf<Constraint>() } as MutableList<Constraint>
    constraints += LengthConstraint(min, max)
    return this
}

/**
 * Constraint for number type
 */
@Suppress("UNCHECKED_CAST")
fun <T : AbstractDescriptor<T>> T.min(value: Int): T {
    val constraints =
        attributes.computeIfAbsent(Attributes.CONSTRAINTS_KEY) { mutableListOf<Constraint>() } as MutableList<Constraint>
    constraints += MinConstraint(value)
    return this
}

/**
 * Constraint for number type
 */
@Suppress("UNCHECKED_CAST")
fun <T : AbstractDescriptor<T>> T.max(value: Int): T {
    val constraints =
        attributes.computeIfAbsent(Attributes.CONSTRAINTS_KEY) { mutableListOf<Constraint>() } as MutableList<Constraint>
    constraints += MaxConstraint(value)
    return this
}

/**
 * Constrain for string type
 */
@Suppress("UNCHECKED_CAST")
fun <T : AbstractDescriptor<T>> T.pattern(pattern: String): T {
    val constraints =
        attributes.computeIfAbsent(Attributes.CONSTRAINTS_KEY) { mutableListOf<Constraint>() } as MutableList<Constraint>
    constraints += PatternConstraint(pattern)
    return this
}

@Suppress("UNCHECKED_CAST")
fun FieldDescriptor.required(): FieldDescriptor {
    val constraints =
        attributes.computeIfAbsent(Attributes.CONSTRAINTS_KEY) { mutableListOf<Constraint>() } as MutableList<Constraint>
    constraints += RequiredConstraint
    return this
}

fun FieldDescriptor.arrayType(type: ArrayItemsType): FieldDescriptor {
    return type(JsonFieldType.ARRAY).attributes(key(Attributes.ARRAY_ITEMS_TYPE_KEY).value(type))
}

private object Attributes {
    const val ENUM_TYPE = "enum"
    const val ENUM_VALUES_KEY = "enumValues"
    const val CONSTRAINTS_KEY = "validationConstraints"
    const val ARRAY_ITEMS_TYPE_KEY = "itemsType"
}

enum class ArrayItemsType {
    OBJECT,
    STRING,
    BOOLEAN,
    NUMBER,
}

// com.epages.restdocs.apispec.jsonschema.ConstraintResolver does not support jakarta 3.x
private object RequiredConstraint : Constraint("javax.validation.constraints.NotNull", emptyMap())

private class SizeConstraint(min: Int?, max: Int?) : Constraint(
    "javax.validation.constraints.Size",
    mapOf("min" to min, "max" to max),
)

private class LengthConstraint(min: Int?, max: Int?) : Constraint(
    "org.hibernate.validator.constraints.Length",
    mapOf("min" to min, "max" to max)
)

private class MinConstraint(value: Int) : Constraint("javax.validation.constraints.Min", mapOf("value" to value))

private class MaxConstraint(value: Int) : Constraint("javax.validation.constraints.Max", mapOf("value" to value))

private class PatternConstraint(pattern: String) : Constraint(
    "javax.validation.constraints.Pattern",
    mapOf("pattern" to pattern)
)

operator fun FieldDescriptors.plus(other: FieldDescriptors): FieldDescriptors {
    return FieldDescriptors(this.fieldDescriptors + other.fieldDescriptors)
}

operator fun FieldDescriptors.plus(other: List<FieldDescriptor>): FieldDescriptors {
    return FieldDescriptors(this.fieldDescriptors + other)
}

operator fun FieldDescriptors.plus(other: FieldDescriptor): FieldDescriptors {
    return FieldDescriptors(this.fieldDescriptors + other)
}

infix fun FieldDescriptors.withPathPrefix(pathPrefix: String): FieldDescriptors {
    return FieldDescriptors(this.fieldDescriptors withPathPrefix pathPrefix)
}

infix fun List<FieldDescriptor>.withPathPrefix(pathPrefix: String): List<FieldDescriptor> {
    return if (pathPrefix.isEmpty()) this else applyPathPrefix(pathPrefix, this)
}
