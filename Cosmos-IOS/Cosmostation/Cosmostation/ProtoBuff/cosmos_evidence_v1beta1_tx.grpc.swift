//
// DO NOT EDIT.
//
// Generated by the protocol buffer compiler.
// Source: cosmos/evidence/v1beta1/tx.proto
//

//
// Copyright 2018, gRPC Authors All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
import GRPC
import NIO
import SwiftProtobuf


/// Msg defines the evidence Msg service.
///
/// Usage: instantiate `Cosmos_Evidence_V1beta1_MsgClient`, then call methods of this protocol to make API calls.
internal protocol Cosmos_Evidence_V1beta1_MsgClientProtocol: GRPCClient {
  var serviceName: String { get }
  var interceptors: Cosmos_Evidence_V1beta1_MsgClientInterceptorFactoryProtocol? { get }

  func submitEvidence(
    _ request: Cosmos_Evidence_V1beta1_MsgSubmitEvidence,
    callOptions: CallOptions?
  ) -> UnaryCall<Cosmos_Evidence_V1beta1_MsgSubmitEvidence, Cosmos_Evidence_V1beta1_MsgSubmitEvidenceResponse>
}

extension Cosmos_Evidence_V1beta1_MsgClientProtocol {
  internal var serviceName: String {
    return "cosmos.evidence.v1beta1.Msg"
  }

  /// SubmitEvidence submits an arbitrary Evidence of misbehavior such as equivocation or
  /// counterfactual signing.
  ///
  /// - Parameters:
  ///   - request: Request to send to SubmitEvidence.
  ///   - callOptions: Call options.
  /// - Returns: A `UnaryCall` with futures for the metadata, status and response.
  internal func submitEvidence(
    _ request: Cosmos_Evidence_V1beta1_MsgSubmitEvidence,
    callOptions: CallOptions? = nil
  ) -> UnaryCall<Cosmos_Evidence_V1beta1_MsgSubmitEvidence, Cosmos_Evidence_V1beta1_MsgSubmitEvidenceResponse> {
    return self.makeUnaryCall(
      path: "/cosmos.evidence.v1beta1.Msg/SubmitEvidence",
      request: request,
      callOptions: callOptions ?? self.defaultCallOptions,
      interceptors: self.interceptors?.makeSubmitEvidenceInterceptors() ?? []
    )
  }
}

internal protocol Cosmos_Evidence_V1beta1_MsgClientInterceptorFactoryProtocol {

  /// - Returns: Interceptors to use when invoking 'submitEvidence'.
  func makeSubmitEvidenceInterceptors() -> [ClientInterceptor<Cosmos_Evidence_V1beta1_MsgSubmitEvidence, Cosmos_Evidence_V1beta1_MsgSubmitEvidenceResponse>]
}

internal final class Cosmos_Evidence_V1beta1_MsgClient: Cosmos_Evidence_V1beta1_MsgClientProtocol {
  internal let channel: GRPCChannel
  internal var defaultCallOptions: CallOptions
  internal var interceptors: Cosmos_Evidence_V1beta1_MsgClientInterceptorFactoryProtocol?

  /// Creates a client for the cosmos.evidence.v1beta1.Msg service.
  ///
  /// - Parameters:
  ///   - channel: `GRPCChannel` to the service host.
  ///   - defaultCallOptions: Options to use for each service call if the user doesn't provide them.
  ///   - interceptors: A factory providing interceptors for each RPC.
  internal init(
    channel: GRPCChannel,
    defaultCallOptions: CallOptions = CallOptions(),
    interceptors: Cosmos_Evidence_V1beta1_MsgClientInterceptorFactoryProtocol? = nil
  ) {
    self.channel = channel
    self.defaultCallOptions = defaultCallOptions
    self.interceptors = interceptors
  }
}

/// Msg defines the evidence Msg service.
///
/// To build a server, implement a class that conforms to this protocol.
internal protocol Cosmos_Evidence_V1beta1_MsgProvider: CallHandlerProvider {
  var interceptors: Cosmos_Evidence_V1beta1_MsgServerInterceptorFactoryProtocol? { get }

  /// SubmitEvidence submits an arbitrary Evidence of misbehavior such as equivocation or
  /// counterfactual signing.
  func submitEvidence(request: Cosmos_Evidence_V1beta1_MsgSubmitEvidence, context: StatusOnlyCallContext) -> EventLoopFuture<Cosmos_Evidence_V1beta1_MsgSubmitEvidenceResponse>
}

extension Cosmos_Evidence_V1beta1_MsgProvider {
  internal var serviceName: Substring { return "cosmos.evidence.v1beta1.Msg" }

  /// Determines, calls and returns the appropriate request handler, depending on the request's method.
  /// Returns nil for methods not handled by this service.
  internal func handle(
    method name: Substring,
    context: CallHandlerContext
  ) -> GRPCServerHandlerProtocol? {
    switch name {
    case "SubmitEvidence":
      return UnaryServerHandler(
        context: context,
        requestDeserializer: ProtobufDeserializer<Cosmos_Evidence_V1beta1_MsgSubmitEvidence>(),
        responseSerializer: ProtobufSerializer<Cosmos_Evidence_V1beta1_MsgSubmitEvidenceResponse>(),
        interceptors: self.interceptors?.makeSubmitEvidenceInterceptors() ?? [],
        userFunction: self.submitEvidence(request:context:)
      )

    default:
      return nil
    }
  }
}

internal protocol Cosmos_Evidence_V1beta1_MsgServerInterceptorFactoryProtocol {

  /// - Returns: Interceptors to use when handling 'submitEvidence'.
  ///   Defaults to calling `self.makeInterceptors()`.
  func makeSubmitEvidenceInterceptors() -> [ServerInterceptor<Cosmos_Evidence_V1beta1_MsgSubmitEvidence, Cosmos_Evidence_V1beta1_MsgSubmitEvidenceResponse>]
}
