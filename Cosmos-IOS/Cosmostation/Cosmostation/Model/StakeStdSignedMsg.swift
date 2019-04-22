//
//  StakeStdSignedMsg.swift
//  Cosmostation
//
//  Created by yongjoo on 22/04/2019.
//  Copyright © 2019 wannabit. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct StakeStdSignedMsg: Codable{
    var chain_id: String = ""
    var account_number: String = ""
    var sequence: String = ""
    var fee: Fee = Fee.init()
    var msgs: Array<StakeMsg> = Array<StakeMsg>()
    var memo: String = ""
    
    //    override init() {}
    init() {}
    
    init(_ dictionary: [String: Any]) {
        self.chain_id = dictionary["chain_id"] as? String ?? ""
        self.account_number = dictionary["account_number"] as? String ?? ""
        self.sequence = dictionary["sequence"] as? String ?? ""
        self.fee = Fee.init(dictionary["fee"] as! [String : Any])
        
        self.msgs.removeAll()
        let rawMsgs = dictionary["msgs"] as! Array<NSDictionary>
        for rawMsg in rawMsgs {
            self.msgs.append(StakeMsg(rawMsg as! [String : Any]))
        }
        
        self.memo = dictionary["memo"] as? String ?? ""
    }
    
    
    //    func getTosignData() -> String {
    //        let encoder = JSONEncoder()
    //        encoder.outputFormatting = .prettyPrinted
    //    }
}
