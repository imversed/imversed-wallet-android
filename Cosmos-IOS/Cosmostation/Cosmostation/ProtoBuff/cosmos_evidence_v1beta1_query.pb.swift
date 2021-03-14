// DO NOT EDIT.
//
// Generated by the Swift generator plugin for the protocol buffer compiler.
// Source: cosmos/evidence/v1beta1/query.proto
//
// For information on using the generated types, please see the documentation:
//   https://github.com/apple/swift-protobuf/

import Foundation
import SwiftProtobuf

// If the compiler emits an error on this type, it is because this file
// was generated by a version of the `protoc` Swift plug-in that is
// incompatible with the version of SwiftProtobuf to which you are linking.
// Please ensure that you are building against the same version of the API
// that was used to generate this file.
fileprivate struct _GeneratedWithProtocGenSwiftVersion: SwiftProtobuf.ProtobufAPIVersionCheck {
  struct _2: SwiftProtobuf.ProtobufAPIVersion_2 {}
  typealias Version = _2
}

/// QueryEvidenceRequest is the request type for the Query/Evidence RPC method.
struct Cosmos_Evidence_V1beta1_QueryEvidenceRequest {
  // SwiftProtobuf.Message conformance is added in an extension below. See the
  // `Message` and `Message+*Additions` files in the SwiftProtobuf library for
  // methods supported on all messages.

  /// evidence_hash defines the hash of the requested evidence.
  var evidenceHash: Data = SwiftProtobuf.Internal.emptyData

  var unknownFields = SwiftProtobuf.UnknownStorage()

  init() {}
}

/// QueryEvidenceResponse is the response type for the Query/Evidence RPC method.
struct Cosmos_Evidence_V1beta1_QueryEvidenceResponse {
  // SwiftProtobuf.Message conformance is added in an extension below. See the
  // `Message` and `Message+*Additions` files in the SwiftProtobuf library for
  // methods supported on all messages.

  /// evidence returns the requested evidence.
  var evidence: Google_Protobuf2_Any {
    get {return _evidence ?? Google_Protobuf2_Any()}
    set {_evidence = newValue}
  }
  /// Returns true if `evidence` has been explicitly set.
  var hasEvidence: Bool {return self._evidence != nil}
  /// Clears the value of `evidence`. Subsequent reads from it will return its default value.
  mutating func clearEvidence() {self._evidence = nil}

  var unknownFields = SwiftProtobuf.UnknownStorage()

  init() {}

  fileprivate var _evidence: Google_Protobuf2_Any? = nil
}

/// QueryEvidenceRequest is the request type for the Query/AllEvidence RPC
/// method.
struct Cosmos_Evidence_V1beta1_QueryAllEvidenceRequest {
  // SwiftProtobuf.Message conformance is added in an extension below. See the
  // `Message` and `Message+*Additions` files in the SwiftProtobuf library for
  // methods supported on all messages.

  /// pagination defines an optional pagination for the request.
  var pagination: Cosmos_Base_Query_V1beta1_PageRequest {
    get {return _pagination ?? Cosmos_Base_Query_V1beta1_PageRequest()}
    set {_pagination = newValue}
  }
  /// Returns true if `pagination` has been explicitly set.
  var hasPagination: Bool {return self._pagination != nil}
  /// Clears the value of `pagination`. Subsequent reads from it will return its default value.
  mutating func clearPagination() {self._pagination = nil}

  var unknownFields = SwiftProtobuf.UnknownStorage()

  init() {}

  fileprivate var _pagination: Cosmos_Base_Query_V1beta1_PageRequest? = nil
}

/// QueryAllEvidenceResponse is the response type for the Query/AllEvidence RPC
/// method.
struct Cosmos_Evidence_V1beta1_QueryAllEvidenceResponse {
  // SwiftProtobuf.Message conformance is added in an extension below. See the
  // `Message` and `Message+*Additions` files in the SwiftProtobuf library for
  // methods supported on all messages.

  /// evidence returns all evidences.
  var evidence: [Google_Protobuf2_Any] = []

  /// pagination defines the pagination in the response.
  var pagination: Cosmos_Base_Query_V1beta1_PageResponse {
    get {return _pagination ?? Cosmos_Base_Query_V1beta1_PageResponse()}
    set {_pagination = newValue}
  }
  /// Returns true if `pagination` has been explicitly set.
  var hasPagination: Bool {return self._pagination != nil}
  /// Clears the value of `pagination`. Subsequent reads from it will return its default value.
  mutating func clearPagination() {self._pagination = nil}

  var unknownFields = SwiftProtobuf.UnknownStorage()

  init() {}

  fileprivate var _pagination: Cosmos_Base_Query_V1beta1_PageResponse? = nil
}

// MARK: - Code below here is support for the SwiftProtobuf runtime.

fileprivate let _protobuf_package = "cosmos.evidence.v1beta1"

extension Cosmos_Evidence_V1beta1_QueryEvidenceRequest: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  static let protoMessageName: String = _protobuf_package + ".QueryEvidenceRequest"
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .standard(proto: "evidence_hash"),
  ]

  mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularBytesField(value: &self.evidenceHash)
      default: break
      }
    }
  }

  func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if !self.evidenceHash.isEmpty {
      try visitor.visitSingularBytesField(value: self.evidenceHash, fieldNumber: 1)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  static func ==(lhs: Cosmos_Evidence_V1beta1_QueryEvidenceRequest, rhs: Cosmos_Evidence_V1beta1_QueryEvidenceRequest) -> Bool {
    if lhs.evidenceHash != rhs.evidenceHash {return false}
    if lhs.unknownFields != rhs.unknownFields {return false}
    return true
  }
}

extension Cosmos_Evidence_V1beta1_QueryEvidenceResponse: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  static let protoMessageName: String = _protobuf_package + ".QueryEvidenceResponse"
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "evidence"),
  ]

  mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularMessageField(value: &self._evidence)
      default: break
      }
    }
  }

  func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if let v = self._evidence {
      try visitor.visitSingularMessageField(value: v, fieldNumber: 1)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  static func ==(lhs: Cosmos_Evidence_V1beta1_QueryEvidenceResponse, rhs: Cosmos_Evidence_V1beta1_QueryEvidenceResponse) -> Bool {
    if lhs._evidence != rhs._evidence {return false}
    if lhs.unknownFields != rhs.unknownFields {return false}
    return true
  }
}

extension Cosmos_Evidence_V1beta1_QueryAllEvidenceRequest: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  static let protoMessageName: String = _protobuf_package + ".QueryAllEvidenceRequest"
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "pagination"),
  ]

  mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularMessageField(value: &self._pagination)
      default: break
      }
    }
  }

  func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if let v = self._pagination {
      try visitor.visitSingularMessageField(value: v, fieldNumber: 1)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  static func ==(lhs: Cosmos_Evidence_V1beta1_QueryAllEvidenceRequest, rhs: Cosmos_Evidence_V1beta1_QueryAllEvidenceRequest) -> Bool {
    if lhs._pagination != rhs._pagination {return false}
    if lhs.unknownFields != rhs.unknownFields {return false}
    return true
  }
}

extension Cosmos_Evidence_V1beta1_QueryAllEvidenceResponse: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  static let protoMessageName: String = _protobuf_package + ".QueryAllEvidenceResponse"
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "evidence"),
    2: .same(proto: "pagination"),
  ]

  mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeRepeatedMessageField(value: &self.evidence)
      case 2: try decoder.decodeSingularMessageField(value: &self._pagination)
      default: break
      }
    }
  }

  func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if !self.evidence.isEmpty {
      try visitor.visitRepeatedMessageField(value: self.evidence, fieldNumber: 1)
    }
    if let v = self._pagination {
      try visitor.visitSingularMessageField(value: v, fieldNumber: 2)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  static func ==(lhs: Cosmos_Evidence_V1beta1_QueryAllEvidenceResponse, rhs: Cosmos_Evidence_V1beta1_QueryAllEvidenceResponse) -> Bool {
    if lhs.evidence != rhs.evidence {return false}
    if lhs._pagination != rhs._pagination {return false}
    if lhs.unknownFields != rhs.unknownFields {return false}
    return true
  }
}
