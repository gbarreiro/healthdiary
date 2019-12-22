//
//  BodyMeasureReading.swift
//  Health Diary
//
//  Created by Guille on 22/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import Foundation

enum BMILevel {
    case UNDERWEIGHT
    case HEALTHY
    case OVERWEIGHT
    case OBESE
}

class BodyMeasureReading {
    let weight: Float!
    let height: Int!
    let timestamp: Date!
    lazy var bmi = weight / pow((Float(height)/100), 2)
    
    var bmiLevel: BMILevel {
        if(bmi<18.5) { return .UNDERWEIGHT }
        if(bmi<24.9) { return .HEALTHY }
        if(bmi<29.9) { return .OVERWEIGHT }
        else { return .OBESE }
    }
    
    init(timestamp: Date){
        self.timestamp = timestamp
        self.height = Int.random(in: 120...200)
        self.weight = Float.random(in: 40...100)
    }
    
    init(weight: Float, height: Int, timestamp: Date){
        self.timestamp = timestamp
        self.weight = weight
        self.height = height
    }
}
