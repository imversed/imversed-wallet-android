// DO NOT EDIT.
//
// Generated by the Swift generator plugin for the protocol buffer compiler.
// Source: ibc/core/channel/v1/genesis.proto
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

/// GenesisState defines the ibc channel submodule's genesis state.
struct Ibc_Core_Channel_V1_GenesisState {
  // SwiftProtobuf.Message conformance is added in an extension below. See the
  // `Message` and `Message+*Additions` files in the SwiftProtobuf library for
  // methods supported on all messages.

  var channels: [Ibc_Core_Channel_V1_IdentifiedChannel] = []

  var acknowledgements: [Ibc_Core_Channel_V1_PacketState] = []

  var commitments: [Ibc_Core_Channel_V1_PacketState] = []

  var receipts: [Ibc_Core_Channel_V1_PacketState] = []

  var sendSequences: [Ibc_Core_Channel_V1_PacketSequence] = []

  var recvSequences: [Ibc_Core_Channel_V1_PacketSequence] = []

  var ackSequences: [Ibc_Core_Channel_V1_PacketSequence] = []

  /// the sequence for the next generated channel identifier
  var nextChannelSequence: UInt64 = 0

  var unknownFields = SwiftProtobuf.UnknownStorage()

  init() {}
}

/// PacketSequence defines the genesis type necessary to retrieve and store
/// next send and receive sequences.
struct Ibc_Core_Channel_V1_PacketSequence {
  // SwiftProtobuf.Message conformance is added in an extension below. See the
  // `Message` and `Message+*Additions` files in the SwiftProtobuf library for
  // methods supported on all messages.

  var portID: String = String()

  var channelID: String = String()

  var sequence: UInt64 = 0

  var unknownFields = SwiftProtobuf.UnknownStorage()

  init() {}
}

// MARK: - Code below here is support for the SwiftProtobuf runtime.

fileprivate let _protobuf_package = "ibc.core.channel.v1"

extension Ibc_Core_Channel_V1_GenesisState: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  static let protoMessageName: String = _protobuf_package + ".GenesisState"
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "channels"),
    2: .same(proto: "acknowledgements"),
    3: .same(proto: "commitments"),
    4: .same(proto: "receipts"),
    5: .standard(proto: "send_sequences"),
    6: .standard(proto: "recv_sequences"),
    7: .standard(proto: "ack_sequences"),
    8: .standard(proto: "next_channel_sequence"),
  ]

  mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeRepeatedMessageField(value: &self.channels)
      case 2: try decoder.decodeRepeatedMessageField(value: &self.acknowledgements)
      case 3: try decoder.decodeRepeatedMessageField(value: &self.commitments)
      case 4: try decoder.decodeRepeatedMessageField(value: &self.receipts)
      case 5: try decoder.decodeRepeatedMessageField(value: &self.sendSequences)
      case 6: try decoder.decodeRepeatedMessageField(value: &self.recvSequences)
      case 7: try decoder.decodeRepeatedMessageField(value: &self.ackSequences)
      case 8: try decoder.decodeSingularUInt64Field(value: &self.nextChannelSequence)
      default: break
      }
    }
  }

  func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if !self.channels.isEmpty {
      try visitor.visitRepeatedMessageField(value: self.channels, fieldNumber: 1)
    }
    if !self.acknowledgements.isEmpty {
      try visitor.visitRepeatedMessageField(value: self.acknowledgements, fieldNumber: 2)
    }
    if !self.commitments.isEmpty {
      try visitor.visitRepeatedMessageField(value: self.commitments, fieldNumber: 3)
    }
    if !self.receipts.isEmpty {
      try visitor.visitRepeatedMessageField(value: self.receipts, fieldNumber: 4)
    }
    if !self.sendSequences.isEmpty {
      try visitor.visitRepeatedMessageField(value: self.sendSequences, fieldNumber: 5)
    }
    if !self.recvSequences.isEmpty {
      try visitor.visitRepeatedMessageField(value: self.recvSequences, fieldNumber: 6)
    }
    if !self.ackSequences.isEmpty {
      try visitor.visitRepeatedMessageField(value: self.ackSequences, fieldNumber: 7)
    }
    if self.nextChannelSequence != 0 {
      try visitor.visitSingularUInt64Field(value: self.nextChannelSequence, fieldNumber: 8)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  static func ==(lhs: Ibc_Core_Channel_V1_GenesisState, rhs: Ibc_Core_Channel_V1_GenesisState) -> Bool {
    if lhs.channels != rhs.channels {return false}
    if lhs.acknowledgements != rhs.acknowledgements {return false}
    if lhs.commitments != rhs.commitments {return false}
    if lhs.receipts != rhs.receipts {return false}
    if lhs.sendSequences != rhs.sendSequences {return false}
    if lhs.recvSequences != rhs.recvSequences {return false}
    if lhs.ackSequences != rhs.ackSequences {return false}
    if lhs.nextChannelSequence != rhs.nextChannelSequence {return false}
    if lhs.unknownFields != rhs.unknownFields {return false}
    return true
  }
}

extension Ibc_Core_Channel_V1_PacketSequence: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  static let protoMessageName: String = _protobuf_package + ".PacketSequence"
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .standard(proto: "port_id"),
    2: .standard(proto: "channel_id"),
    3: .same(proto: "sequence"),
  ]

  mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularStringField(value: &self.portID)
      case 2: try decoder.decodeSingularStringField(value: &self.channelID)
      case 3: try decoder.decodeSingularUInt64Field(value: &self.sequence)
      default: break
      }
    }
  }

  func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if !self.portID.isEmpty {
      try visitor.visitSingularStringField(value: self.portID, fieldNumber: 1)
    }
    if !self.channelID.isEmpty {
      try visitor.visitSingularStringField(value: self.channelID, fieldNumber: 2)
    }
    if self.sequence != 0 {
      try visitor.visitSingularUInt64Field(value: self.sequence, fieldNumber: 3)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  static func ==(lhs: Ibc_Core_Channel_V1_PacketSequence, rhs: Ibc_Core_Channel_V1_PacketSequence) -> Bool {
    if lhs.portID != rhs.portID {return false}
    if lhs.channelID != rhs.channelID {return false}
    if lhs.sequence != rhs.sequence {return false}
    if lhs.unknownFields != rhs.unknownFields {return false}
    return true
  }
}
