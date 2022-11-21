/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package aws.sdk.kotlin.codegen.protocols.endpoints

import aws.sdk.kotlin.codegen.AwsServiceConfigIntegration
import software.amazon.smithy.codegen.core.Symbol
import software.amazon.smithy.kotlin.codegen.KotlinSettings
import software.amazon.smithy.kotlin.codegen.core.KotlinWriter
import software.amazon.smithy.kotlin.codegen.core.withBlock
import software.amazon.smithy.kotlin.codegen.model.buildSymbol
import software.amazon.smithy.kotlin.codegen.model.defaultName
import software.amazon.smithy.kotlin.codegen.rendering.endpoints.EndpointParametersGenerator
import software.amazon.smithy.kotlin.codegen.rendering.protocol.ProtocolGenerator
import software.amazon.smithy.rulesengine.language.syntax.parameters.Parameter
import aws.sdk.kotlin.codegen.customization.s3.ClientConfigIntegration as S3ClientConfigIntegration
import aws.sdk.kotlin.codegen.customization.s3control.ClientConfigIntegration as S3ControlClientConfigIntegration

/**
 * Render binding of AWS SDK endpoint parameter builtins. In practice, all of these values are sourced from the client
 * config.
 */
fun renderBindAwsBuiltins(ctx: ProtocolGenerator.GenerationContext, writer: KotlinWriter, builtinParams: List<Parameter>) {
    writer.withBlock(
        "internal fun #T.Builder.bindAwsBuiltins(config: #T.Config) {",
        "}",
        EndpointParametersGenerator.getSymbol(ctx.settings),
        ctx.symbolProvider.toSymbol(ctx.service),
    ) {
        builtinParams.forEach {
            when (it.builtIn.get()) {
                "AWS::Region" -> renderBasicConfigBinding(writer, it, AwsServiceConfigIntegration.RegionProp.propertyName)
                "AWS::UseFIPS" -> renderBasicConfigBinding(writer, it, AwsServiceConfigIntegration.UseFipsProp.propertyName)
                "AWS::UseDualStack" -> renderBasicConfigBinding(writer, it, AwsServiceConfigIntegration.UseDualStackProp.propertyName)

                "AWS::S3::Accelerate" -> renderBasicConfigBinding(writer, it, S3ClientConfigIntegration.EnableAccelerateProp.propertyName)
                "AWS::S3::ForcePathStyle" -> renderBasicConfigBinding(writer, it, S3ClientConfigIntegration.ForcePathStyleProp.propertyName)
                "AWS::S3::DisableMultiRegionAccessPoints" -> renderBasicConfigBinding(writer, it, S3ClientConfigIntegration.DisableMrapProp.propertyName)
                "AWS::S3::UseArnRegion" -> renderBasicConfigBinding(writer, it, S3ClientConfigIntegration.DisableMrapProp.propertyName)
                "AWS::S3Control::UseArnRegion" -> renderBasicConfigBinding(writer, it, S3ControlClientConfigIntegration.UseArnRegionProp.propertyName)

                "SDK::Endpoint" ->
                    writer.write("#L = config.#L?.toString()", it.defaultName(), AwsServiceConfigIntegration.EndpointUrlProp.propertyName)

                // as a newer SDK we do NOT support these values, they are always false
                "AWS::S3::UseGlobalEndpoint", "AWS::STS::UseGlobalEndpoint" ->
                    writer.write("#L = false", it.defaultName())
            }
        }
    }
}

fun bindAwsBuiltinsSymbol(settings: KotlinSettings): Symbol =
    buildSymbol {
        name = "bindAwsBuiltins"
        namespace = "${settings.pkg.name}.endpoints.internal"
    }

private fun renderBasicConfigBinding(writer: KotlinWriter, param: Parameter, configMember: String) {
    writer.write("#L = config.#L", param.defaultName(), configMember)
}
