//
//  BodyMeasureReading+Extension.swift
//  Health Diary
//
//  Created by Guille on 03/01/2020.
//  Copyright © 2020 Guillermo Barreiro. All rights reserved.
//

import Foundation
import CoreData

/**
    Represents a body measure reading, this means, the sampling of the user's weight and height in a specific moment. The weight is stored as a decimal number in kg, and the height as an integer in cm.
*/
extension BodyMeasureReading {
    
    enum BMILevel {
        case UNDERWEIGHT
        case HEALTHY
        case OVERWEIGHT
        case OBESE
    }
    
    /**
     Body measure index is calculated as kg / (m)².
     This index gives a ratio of how low or high is the weight of a person.
     
     # Risk levels
     * <18.5: Underweight
     * 18.5 - 24.9: healthy
     * 25 - 29.9: overweight
     * 30 - 39.9: obese
     */
    var bmi: Float {
        return BodyMeasureReading.calculateBmi(weight: self.weight, height: self.height)
    }

    var bmiLevel: BMILevel {
        BodyMeasureReading.calculateBmiLevel(bmi: self.bmi)
    }
    
    static func calculateBmi(weight: Float, height: Int16) -> Float{
        return weight / pow((Float(height)/100), 2)
    }
    
    static func calculateBmiLevel(bmi: Float) -> BMILevel{
        if(bmi<18.5) { return .UNDERWEIGHT }
        if(bmi<24.9) { return .HEALTHY }
        if(bmi<29.9) { return .OVERWEIGHT }
        else { return .OBESE }
    }
    
    /**
        Random constructor. Creates a body measure reading with random weight and height.
     */
    convenience init(context: NSManagedObjectContext, timestamp: Date){
        self.init(context: context)
        self.timestamp = timestamp
        self.height = Int16.random(in: 120...200)
        self.weight = Float.random(in: 40...100)
    }
    
    /**
        Creates a body measure with the specififed weight and height.
     */
    convenience init(context: NSManagedObjectContext, weight: Float, height: Int, timestamp: Date){
        self.init(context: context)
        self.timestamp = timestamp
        self.weight = weight
        self.height = Int16(height)
    }
    
    struct AverageBodyMeasure {
        let weight: Float!
        let height: Int!
        let bmi: Float!
        let riskLevel: BMILevel!
    }
    
    /**
        Calculates the average weight, height and BMI value of a list of BodyMeasureReading records
         - Parameter records:  list with BodyMeasureReading records, whose average values you desire to calculate
         - Returns: Struct with weight, height and BMI value.
     */
    static func calculateAverage(records: [BodyMeasureReading]) -> AverageBodyMeasure? {
        if records.count > 0 {
            var weight:Float = 0.0, height:Int16 = 0
            for record in records{
                weight += record.weight
                height += record.height
            }
            weight /= Float(records.count)
            height /= Int16(records.count)
            let bmi = BodyMeasureReading.calculateBmi(weight: weight, height: height)
            let riskLevel = BodyMeasureReading.calculateBmiLevel(bmi: bmi)
            
            let average = AverageBodyMeasure(weight: weight, height: Int(height), bmi: bmi, riskLevel: riskLevel)
            return average
        } else {
            return nil
        }
  
    }
    
}
