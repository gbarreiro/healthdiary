//
//  BloodPressureReading+Extension.swift
//  Health Diary
//
//  Created by Guille on 03/01/2020.
//  Copyright © 2020 Guillermo Barreiro. All rights reserved.
//

import Foundation
import CoreData
import GameplayKit

/**
    Represents a blood pressure reading, this means, the sampling of the user's systolic and diastolic blood pressure values in a specific moment. Both systolic and diastolic values are stored as Integer values, expressed in mmHg.
 
    This extension adds some computed values and random values generation to the BloodPressureReading class generated by Core Data.
*/
extension BloodPressureReading {
    
    // MARK: Random values generation
    static let randomSystolicGenerator = GKGaussianDistribution(randomSource: GKRandomSource(), mean: 120, deviation: 20)
    static let randomDiastolicGenerator = GKGaussianDistribution(randomSource: GKRandomSource(), mean: 70, deviation: 20)
    
    // MARK: Risk level
    enum RiskLevel {
        case NORMAL
        case ELEVATED
        case HIGH
        case HYPERTENSIVE
    }
    
    /**
    Blood pressure risk level, based on [heart.org](https://www.heart.org/en/health-topics/high-blood-pressure/understanding-blood-pressure-readings) ranges
    
    # Risk levels
    * Systolic  < 120 and Diastolic < 80: Normal
    * Systolic < 129 and Diastolic < 80: Elevated
    * Systolic < 180 and Diastolic < 120: High
    * Systolic >   180 and Diastolic > 120: Hypertensive
    */
    var riskLevel: RiskLevel{
        return BloodPressureReading.calculateRiskLevel(systolic: self.systolic, diastolic: self.diastolic)
    }
    
    // Static function, so it can be used from the static calculateAverage() method
    static func calculateRiskLevel(systolic: Int16, diastolic: Int16) -> RiskLevel{
        if (systolic < 120 && diastolic < 80) {return .NORMAL}
        if (systolic < 129 && diastolic < 80) {return .ELEVATED}
        if (systolic < 180 && diastolic < 120) {return .HIGH}
        else {return .HYPERTENSIVE}
    }
    
    // MARK: Convenience constructors
    
    /**
       Random constructor. Creates a blood pressure reading with gaussian random values.
    */
    convenience init(context: NSManagedObjectContext, timestamp: Date){
        // Random diastolic and systolic values
        self.init(context: context)
        self.timestamp = timestamp
        self.systolic = Int16(BloodPressureReading.randomSystolicGenerator.nextInt())
        self.diastolic = Int16(BloodPressureReading.randomDiastolicGenerator.nextInt())
    }
   
    /**
       Creates a blood pressure reading with the specififed systolic and diastolic values.
    */
    convenience init(context: NSManagedObjectContext, systolic: Int, diastolic: Int, timestamp: Date){
        self.init(context: context)
        self.systolic = Int16(systolic)
        self.diastolic = Int16(diastolic)
        self.timestamp = timestamp
    }
    
    // MARK: Calculate average
    
    /**
     Represents the three average values of a list of blood pressure readings
     */
    struct AverageBloodPressure {
        let systolic: Int!
        let diastolic: Int!
        let riskLevel: RiskLevel!
    }

   /**
       Calculates the average systolic and disastolic value of a list of BloodPressureReading records
        - Parameter records:  list with BloodPressureReading records, whose average values you desire to calculate
        - Returns: Struct with systolic and diastolic values and risk level.
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
