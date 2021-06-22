/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0.
 */

package aws.smithy.kotlin.runtime.serde.xml

import aws.smithy.kotlin.runtime.serde.*

class SimpleStructClass {
    var x: Int? = null
    var y: Int? = null
    var z: String? = null

    // Only for testing, not serialization
    var unknownFieldCount: Int = 0

    companion object {
        val X_DESCRIPTOR = SdkFieldDescriptor(SerialKind.Integer, XmlSerialName("x"))
        val Y_DESCRIPTOR = SdkFieldDescriptor(SerialKind.Integer, XmlSerialName("y"))
        val Z_DESCRIPTOR = SdkFieldDescriptor(SerialKind.String, XmlSerialName("z"), XmlAttribute)
        val OBJ_DESCRIPTOR = SdkObjectDescriptor.build {
            trait(XmlSerialName("payload"))
            field(X_DESCRIPTOR)
            field(Y_DESCRIPTOR)
            field(Z_DESCRIPTOR)
        }

        suspend fun deserialize(deserializer: Deserializer): SimpleStructClass {
            val result = SimpleStructClass()
            deserializer.deserializeStruct(OBJ_DESCRIPTOR) {
                loop@ while (true) {
                    when (findNextFieldIndex()) {
                        X_DESCRIPTOR.index -> result.x = deserializeInt()
                        Y_DESCRIPTOR.index -> result.y = deserializeInt()
                        Z_DESCRIPTOR.index -> result.z = deserializeString()
                        null -> break@loop
                        else -> throw DeserializationException(IllegalStateException("unexpected field in BasicStructTest deserializer"))
                    }
                }
            }
            return result
        }
    }
}

class SimpleStructOfStringsClass {
    var x: String? = null
    var y: String? = null

    // Only for testing, not serialization
    var unknownFieldCount: Int = 0

    companion object {
        val X_DESCRIPTOR = SdkFieldDescriptor(SerialKind.Integer, XmlSerialName("x"))
        val Y_DESCRIPTOR = SdkFieldDescriptor(SerialKind.Integer, XmlSerialName("y"))
        val OBJ_DESCRIPTOR = SdkObjectDescriptor.build {
            trait(XmlSerialName("payload"))
            field(X_DESCRIPTOR)
            field(Y_DESCRIPTOR)
        }

        suspend fun deserialize(deserializer: Deserializer): SimpleStructOfStringsClass {
            val result = SimpleStructOfStringsClass()
            deserializer.deserializeStruct(OBJ_DESCRIPTOR) {
                loop@ while (true) {
                    when (findNextFieldIndex()) {
                        X_DESCRIPTOR.index -> result.x = deserializeString()
                        Y_DESCRIPTOR.index -> result.y = deserializeString()
                        null -> break@loop
                        else -> throw DeserializationException(IllegalStateException("unexpected field in BasicStructTest deserializer"))
                    }
                }
            }
            return result
        }
    }
}

class StructWithAttribsClass {
    var x: Int? = null
    var y: Int? = null
    var unknownFieldCount: Int = 0

    companion object {
        val X_DESCRIPTOR = SdkFieldDescriptor(SerialKind.Integer, XmlSerialName("x"), XmlAttribute)
        val Y_DESCRIPTOR = SdkFieldDescriptor(SerialKind.Integer, XmlSerialName("y"), XmlAttribute)
        val OBJ_DESCRIPTOR = SdkObjectDescriptor.build {
            trait(XmlSerialName("payload"))
            field(X_DESCRIPTOR)
            field(Y_DESCRIPTOR)
        }

        suspend fun deserialize(deserializer: Deserializer): StructWithAttribsClass {
            val result = StructWithAttribsClass()
            deserializer.deserializeStruct(OBJ_DESCRIPTOR) {
                loop@ while (true) {
                    when (findNextFieldIndex()) {
                        X_DESCRIPTOR.index -> result.x = deserializeInt()
                        Y_DESCRIPTOR.index -> result.y = deserializeInt()
                        null -> break@loop
                        Deserializer.FieldIterator.UNKNOWN_FIELD -> {
                            result.unknownFieldCount++
                            skipValue()
                        }
                        else -> throw DeserializationException(IllegalStateException("unexpected field in BasicStructTest deserializer"))
                    }
                }
            }
            return result
        }
    }
}

class StructWithMultiAttribsAndTextValClass {
    var x: Int? = null
    var y: Int? = null
    var txt: String? = null
    var unknownFieldCount: Int = 0

    companion object {
        val X_DESCRIPTOR = SdkFieldDescriptor(SerialKind.Integer, XmlSerialName("xval"), XmlAttribute)
        val Y_DESCRIPTOR = SdkFieldDescriptor(SerialKind.Integer, XmlSerialName("yval"), XmlAttribute)
        val TXT_DESCRIPTOR = SdkFieldDescriptor(SerialKind.Integer, XmlSerialName("x"))
        val OBJ_DESCRIPTOR = SdkObjectDescriptor.build {
            trait(XmlSerialName("payload"))
            field(TXT_DESCRIPTOR)
            field(X_DESCRIPTOR)
            field(Y_DESCRIPTOR)
        }

        suspend fun deserialize(deserializer: Deserializer): StructWithMultiAttribsAndTextValClass {
            val result = StructWithMultiAttribsAndTextValClass()
            deserializer.deserializeStruct(OBJ_DESCRIPTOR) {
                loop@ while (true) {
                    when (findNextFieldIndex()) {
                        X_DESCRIPTOR.index -> result.x = deserializeInt()
                        Y_DESCRIPTOR.index -> result.y = deserializeInt()
                        TXT_DESCRIPTOR.index -> result.txt = deserializeString()
                        null -> break@loop
                        Deserializer.FieldIterator.UNKNOWN_FIELD -> {
                            result.unknownFieldCount++
                            skipValue()
                        }
                        else -> throw DeserializationException(IllegalStateException("unexpected field in BasicStructTest deserializer"))
                    }
                }
            }
            return result
        }
    }
}

class RecursiveShapesInputOutput private constructor(builder: BuilderImpl) {
    val nested: RecursiveShapesInputOutputNested1? = builder.nested

    companion object {
        fun builder(): Builder = BuilderImpl()

        fun dslBuilder(): DslBuilder = BuilderImpl()

        operator fun invoke(block: DslBuilder.() -> kotlin.Unit): RecursiveShapesInputOutput = BuilderImpl().apply(block).build()
    }

    override fun toString(): kotlin.String = buildString {
        append("RecursiveShapesInputOutput(")
        append("nested=$nested)")
    }

    override fun hashCode(): kotlin.Int {
        var result = nested?.hashCode() ?: 0
        return result
    }

    override fun equals(other: kotlin.Any?): kotlin.Boolean {
        if (this === other) return true

        other as RecursiveShapesInputOutput

        if (nested != other.nested) return false

        return true
    }

    fun copy(block: DslBuilder.() -> kotlin.Unit = {}): RecursiveShapesInputOutput = BuilderImpl(this).apply(block).build()

    interface Builder {
        fun build(): RecursiveShapesInputOutput
        fun nested(nested: RecursiveShapesInputOutputNested1): Builder
    }

    interface DslBuilder {
        var nested: RecursiveShapesInputOutputNested1?

        fun build(): RecursiveShapesInputOutput
        fun nested(block: RecursiveShapesInputOutputNested1.DslBuilder.() -> kotlin.Unit) {
            this.nested = RecursiveShapesInputOutputNested1.invoke(block)
        }
    }

    private class BuilderImpl() : Builder, DslBuilder {
        override var nested: RecursiveShapesInputOutputNested1? = null

        constructor(x: RecursiveShapesInputOutput) : this() {
            this.nested = x.nested
        }

        override fun build(): RecursiveShapesInputOutput = RecursiveShapesInputOutput(this)
        override fun nested(nested: RecursiveShapesInputOutputNested1): Builder = apply { this.nested = nested }
    }
}

class RecursiveShapesInputOutputNested1 private constructor(builder: BuilderImpl) {
    val foo: String? = builder.foo
    val nested: RecursiveShapesInputOutputNested2? = builder.nested

    companion object {
        fun builder(): Builder = BuilderImpl()

        fun dslBuilder(): DslBuilder = BuilderImpl()

        operator fun invoke(block: DslBuilder.() -> kotlin.Unit): RecursiveShapesInputOutputNested1 = BuilderImpl().apply(block).build()
    }

    override fun toString(): kotlin.String = buildString {
        append("RecursiveShapesInputOutputNested1(")
        append("foo=$foo,")
        append("nested=$nested)")
    }

    override fun hashCode(): kotlin.Int {
        var result = foo?.hashCode() ?: 0
        result = 31 * result + (nested?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: kotlin.Any?): kotlin.Boolean {
        if (this === other) return true

        other as RecursiveShapesInputOutputNested1

        if (foo != other.foo) return false
        if (nested != other.nested) return false

        return true
    }

    fun copy(block: DslBuilder.() -> kotlin.Unit = {}): RecursiveShapesInputOutputNested1 = BuilderImpl(this).apply(block).build()

    interface Builder {
        fun build(): RecursiveShapesInputOutputNested1
        fun foo(foo: String): Builder
        fun nested(nested: RecursiveShapesInputOutputNested2): Builder
    }

    interface DslBuilder {
        var foo: String?
        var nested: RecursiveShapesInputOutputNested2?

        fun build(): RecursiveShapesInputOutputNested1
        fun nested(block: RecursiveShapesInputOutputNested2.DslBuilder.() -> kotlin.Unit) {
            this.nested = RecursiveShapesInputOutputNested2.invoke(block)
        }
    }

    private class BuilderImpl() : Builder, DslBuilder {
        override var foo: String? = null
        override var nested: RecursiveShapesInputOutputNested2? = null

        constructor(x: RecursiveShapesInputOutputNested1) : this() {
            this.foo = x.foo
            this.nested = x.nested
        }

        override fun build(): RecursiveShapesInputOutputNested1 = RecursiveShapesInputOutputNested1(this)
        override fun foo(foo: String): Builder = apply { this.foo = foo }
        override fun nested(nested: RecursiveShapesInputOutputNested2): Builder = apply { this.nested = nested }
    }
}

class RecursiveShapesInputOutputNested2 private constructor(builder: BuilderImpl) {
    val bar: String? = builder.bar
    val recursiveMember: RecursiveShapesInputOutputNested1? = builder.recursiveMember

    companion object {
        fun builder(): Builder = BuilderImpl()

        fun dslBuilder(): DslBuilder = BuilderImpl()

        operator fun invoke(block: DslBuilder.() -> kotlin.Unit): RecursiveShapesInputOutputNested2 = BuilderImpl().apply(block).build()
    }

    override fun toString(): kotlin.String = buildString {
        append("RecursiveShapesInputOutputNested2(")
        append("bar=$bar,")
        append("recursiveMember=$recursiveMember)")
    }

    override fun hashCode(): kotlin.Int {
        var result = bar?.hashCode() ?: 0
        result = 31 * result + (recursiveMember?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: kotlin.Any?): kotlin.Boolean {
        if (this === other) return true

        other as RecursiveShapesInputOutputNested2

        if (bar != other.bar) return false
        if (recursiveMember != other.recursiveMember) return false

        return true
    }

    fun copy(block: DslBuilder.() -> kotlin.Unit = {}): RecursiveShapesInputOutputNested2 = BuilderImpl(this).apply(block).build()

    interface Builder {
        fun build(): RecursiveShapesInputOutputNested2
        fun bar(bar: String): Builder
        fun recursiveMember(recursiveMember: RecursiveShapesInputOutputNested1): Builder
    }

    interface DslBuilder {
        var bar: String?
        var recursiveMember: RecursiveShapesInputOutputNested1?

        fun build(): RecursiveShapesInputOutputNested2
        fun recursiveMember(block: RecursiveShapesInputOutputNested1.DslBuilder.() -> kotlin.Unit) {
            this.recursiveMember = RecursiveShapesInputOutputNested1.invoke(block)
        }
    }

    private class BuilderImpl() : Builder, DslBuilder {
        override var bar: String? = null
        override var recursiveMember: RecursiveShapesInputOutputNested1? = null

        constructor(x: RecursiveShapesInputOutputNested2) : this() {
            this.bar = x.bar
            this.recursiveMember = x.recursiveMember
        }

        override fun build(): RecursiveShapesInputOutputNested2 = RecursiveShapesInputOutputNested2(this)
        override fun bar(bar: String): Builder = apply { this.bar = bar }
        override fun recursiveMember(recursiveMember: RecursiveShapesInputOutputNested1): Builder = apply { this.recursiveMember = recursiveMember }
    }
}

/*
    @xmlNamespace(uri: "http://foo.com")
    structure XmlNamespacesInputOutput {
        nested: XmlNamespaceNested
    }

    // Ignored since it's not at the top-level
    @xmlNamespace(uri: "http://foo.com")
    structure XmlNamespaceNested {
        @xmlNamespace(uri: "http://baz.com", prefix: "baz")
        foo: String,

        @xmlNamespace(uri: "http://qux.com")
        values: XmlNamespacedList
    }

    list XmlNamespacedList {
        @xmlNamespace(uri: "http://bux.com")
        member: String,
    }
*/
class XmlNamespacesRequest(val nested: XmlNamespaceNested?) {
    companion object {
        private val NESTED_DESCRIPTOR = SdkFieldDescriptor(SerialKind.Struct, XmlSerialName("nested"))
        private val OBJ_DESCRIPTOR = SdkObjectDescriptor.build {
            trait(XmlSerialName("XmlNamespacesInputOutput"))
            trait(XmlNamespace("http://foo.com"))
            field(NESTED_DESCRIPTOR)
        }
    }

    fun serialize(serializer: Serializer) {
        serializer.serializeStruct(OBJ_DESCRIPTOR) {
            nested?.let { field(NESTED_DESCRIPTOR, XmlNamespaceNestedDocumentSerializer(it)) }
        }
    }
}

data class XmlNamespaceNested(
    val foo: String? = null,
    val values: List<String>? = null,
)

internal class XmlNamespaceNestedDocumentSerializer(val input: XmlNamespaceNested) : SdkSerializable {

    companion object {
        private val FOO_DESCRIPTOR = SdkFieldDescriptor(SerialKind.String, XmlSerialName("foo"), XmlNamespace("http://baz.com", "baz"))
        private val VALUES_DESCRIPTOR = SdkFieldDescriptor(SerialKind.List, XmlSerialName("values"), XmlNamespace("http://qux.com"), XmlCollectionValueNamespace("http://bux.com"))
        private val OBJ_DESCRIPTOR = SdkObjectDescriptor.build {
            trait(XmlSerialName("XmlNamespaceNested"))
            trait(XmlNamespace("http://foo.com"))
            field(FOO_DESCRIPTOR)
            field(VALUES_DESCRIPTOR)
        }
    }

    override fun serialize(serializer: Serializer) {
        serializer.serializeStruct(OBJ_DESCRIPTOR) {
            input.foo?.let { field(FOO_DESCRIPTOR, it) }
            if (input.values != null) {
                listField(VALUES_DESCRIPTOR) {
                    for (el0 in input.values) {
                        serializeString(el0)
                    }
                }
            }
        }
    }
}