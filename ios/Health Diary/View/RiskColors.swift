//
//  RiskColors.swift
//  Health Diary
//
//  Created by Guille on 15/01/2020.
//  Copyright © 2020 Guillermo Barreiro. All rights reserved.
//

import Foundation
import UIKit

/**
    Risk colors for the blood pressure and BMI values, used for the average display and the record list.
 */
class RiskColors {
    /**
     Colors for the blood pressure average values, depending on the risk level.
     */
    public static let bloodPressureRiskColors: [BloodPressureReading.RiskLevel: UIColor] = [
        BloodPressureReading.RiskLevel.NORMAL: UIColor.green,
        BloodPressureReading.RiskLevel.ELEVATED: UIColor.yellow,
        BloodPressureReading.RiskLevel.HIGH: UIColor.orange,
        BloodPressureReading.RiskLevel.HYPERTENSIVE: UIColor.red
    ]
    
    /**
     Colors for the BMI values, depending on the risk level.
     */
    public static let bmiRiskColors: [BodyMeasureReading.BMILevel: UIColor] = [
        BodyMeasureReading.BMILevel.HEALTHY: UIColor.green,
        BodyMeasureReading.BMILevel.UNDERWEIGHT: UIColor.orange,
        BodyMeasureReading.BMILevel.OVERWEIGHT: UIColor.orange,
        BodyMeasureReading.BMILevel.OBESE: UIColor.red
    ]
    
}
