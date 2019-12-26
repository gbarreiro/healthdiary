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

/**
    Represents a body measure reading, this means, the sampling of the user's weight and height in a specific moment. The weight is stored as a decimal number in kg, and the height as an integer in cm.
*/
class BodyMeasureReading {
    let weight: Float!
    let height: Int!
    let timestamp: Date!
    
    /**
     Body measure index is calculated as kg / (m)².
     This index gives a ratio of how low or high is the weight of a person.
     
     # Risk levels
     * <18.5: Underweight
     * 18.5 - 24.9: healthy
     * 25 - 29.9: overweight
     * 30 - 39.9: obese
     */
    lazy var bmi = weight / pow((Float(height)/100), 2)
    
    var bmiLevel: BMILevel {
        if(bmi<18.5) { return .UNDERWEIGHT }
        if(bmi<24.9) { return .HEALTHY }
        if(bmi<29.9) { return .OVERWEIGHT }
        else { return .OBESE }
    }
    
    /**
        Random constructor. Creates a body measure reading with random weight and height.
     */
    init(timestamp: Date){
        self.timestamp = timestamp
        self.height = Int.random(in: 120...200)
        self.weight = Float.random(in: 40...100)
    }
    
    /**
        Creates a body measure with the specififed weight and height.
     */
    init(weight: Float, height: Int, timestamp: Date){
        self.timestamp = timestamp
        self.weight = weight
        self.height = height
    }
    
    /**
        Creates a record with the average values from a list of readings
        - Parameter records:  list with BodyMeasure records, whose average values you desire to calculate
     */
    init(records: [BodyMeasureReading]) {
        var weight:Float = 0.0, height = 0
        for record in records{
            weight += record.weight
            height += record.height
        }
        weight /= Float(records.count)
        height /= records.count
        
        self.weight = weight
        self.height = height
        self.timestamp = Date() // date is not relevant for an average object
        
    }
}
