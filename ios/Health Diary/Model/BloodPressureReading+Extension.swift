//
//  BloodPressureReading+Extension.swift
//  Health Diary
//
//  Created by Guille on 03/01/2020.
//  Copyright © 2020 Guillermo Barreiro. All rights reserved.
//

import Foundation
import CoreData

/**
    Represents a blood pressure reading, this means, the sampling of the user's systolic and diastolic blood pressure values in a specific moment. Both systolic and diastolic values are stored as Integer values, expressed in mmHg
*/
extension BloodPressureReading {
    enum RiskLevel {
        case NORMAL
        case ELEVATED
        case HIGH
        case HYPERTENSIVE
    }
    
    /**
    Blood pressure risk level, based on heart.org ranges
    
    # Risk levels
    * Systolic  < 120 and Diastolic < 80: Normal
    * Systolic < 129 and Diastolic < 80: Elevated
    * Systolic < 180 and Diastolic < 120: High
    * Systolic >   180 and Diastolic > 120: Hypertensive
    */
    var riskLevel: RiskLevel{
        return BloodPressureReading.calculateRiskLevel(systolic: self.systolic, diastolic: self.diastolic)
    }
    
    static func calculateRiskLevel(systolic: Int16, diastolic: Int16) -> RiskLevel{
        if (systolic < 120 && diastolic < 80) {return .NORMAL}
        if (systolic < 129 && diastolic < 80) {return .ELEVATED}
        if (systolic < 180 && diastolic < 120) {return .HIGH}
        else {return .HYPERTENSIVE}
    }
    
    /**
       Random constructor. Creates a blood pressure reading with random values.
    */
    convenience init(context: NSManagedObjectContext, timestamp: Date){
        // Random diastolic and systolic values
        self.init(context: context)
        self.timestamp = timestamp
        self.systolic = Int16.random(in: 100...180)
        self.diastolic = Int16.random(in: 60...105)
    }
   
    /**
       Creates a blood pressure  measure with the specififed systolic and diastolic values.
    */
    convenience init(context: NSManagedObjectContext, systolic: Int, diastolic: Int, timestamp: Date){
        self.init(context: context)
        self.systolic = Int16(systolic)
        self.diastolic = Int16(diastolic)
        self.timestamp = timestamp
    }
    
    struct AverageBloodPressure {
        let systolic: Int!
        let diastolic: Int!
        let riskLevel: RiskLevel!
    }
   

   /**
       Calculates the average systolic and disastolic value of a list of BloodPressureReading records
        - Parameter records:  list with BloodPressureReading records, whose average values you desire to calculate
        - Returns: Tuple with systolic and diastolic value, as Int values (systolic, diastolic)
    */
    static func calculateAverage(records: [BloodPressureReading]) -> AverageBloodPressure? {
        if records.count > 0 {
            var systolic:Int16 = 0, diastolic:Int16 = 0
            for record in records{
                systolic += record.systolic
                diastolic += record.diastolic
            }
            systolic /= Int16(records.count)
            diastolic /= Int16(records.count)
            let riskLevel = BloodPressureReading.calculateRiskLevel(systolic: systolic, diastolic: diastolic)
            
            let average = AverageBloodPressure(systolic: Int(systolic), diastolic: Int(diastolic), riskLevel: riskLevel)
            return average
        } else {
            return nil // empty list of readings
        }

       
   }
    
}
