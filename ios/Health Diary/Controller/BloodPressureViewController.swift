//
//  BloodPressureViewController.swift
//  Health Diary
//
//  Created by Guille on 10/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import UIKit

class BloodPressureViewController: UIViewController {
    
    // Colors for the average values, depending on the risk level
    private static let riskColors: [BloodPressureReading.RiskLevel: UIColor] = [
        BloodPressureReading.RiskLevel.NORMAL: UIColor.green,
        BloodPressureReading.RiskLevel.ELEVATED: UIColor.yellow,
        BloodPressureReading.RiskLevel.HIGH: UIColor.orange,
        BloodPressureReading.RiskLevel.HYPERTENSIVE: UIColor.red
    ]
    
    // MARK: UIOutlets
    @IBOutlet weak var systolicInput: UITextField!
    @IBOutlet weak var diastolicInput: UITextField!
    @IBOutlet weak var systolicAverage: UILabel!
    @IBOutlet weak var diastolicAverage: UILabel!
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var randomSwitch: UISwitch!
    
    // MARK: Data sources
    private var records: [BloodPressureReading] = []
    
    // MARK: UIActions
    @IBAction func updateUIStatus() {
        if(randomSwitch.isOn){
            // With the random switch on, text inputs are always disabled and the register button is always enabled
            systolicInput.isEnabled = false
            diastolicInput.isEnabled = false
            registerButton.isEnabled = true
        
        }else{
            // With the random switch off, text inputs are always enabled
            systolicInput.isEnabled = true
            diastolicInput.isEnabled = true
            
            // With the random switch off, the register button will only be enabled if both text inputs aren't empty
            registerButton.isEnabled = (!(systolicInput.text!.isEmpty) && !(diastolicInput.text!.isEmpty))
        }
        
    }

    
    @IBAction func registerValues() {
        var reading: BloodPressureReading?
        
        if(randomSwitch.isOn){
            // Random values for the blood pressure reading
            reading = BloodPressureReading(timestamp: Date())
        } else{
            // User introduced values for the blood pressure reading
            if let systolic = Int(systolicInput.text!), let diastolic = Int(diastolicInput.text!){
                if systolic > 0 && diastolic > 0 {
                    // Introduced systolic and diastolic values are positive integers
                    reading = BloodPressureReading(systolic: systolic, diastolic: diastolic, timestamp: Date())
                }
            }
        }

        // Registers the reading in the array
        if let reading = reading{
            records.append(reading)
            
            // Update the average values
            let averageValues = BloodPressureReading(records: records)
            let riskColor = BloodPressureViewController.riskColors[averageValues.riskLevel]
            systolicAverage.text = String(averageValues.systolic)
            diastolicAverage.text = String(averageValues.diastolic)
            systolicAverage.textColor = riskColor
            diastolicAverage.textColor = riskColor
        }

    }
    
    
    // MARK: ViewController lifecycle methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
}
