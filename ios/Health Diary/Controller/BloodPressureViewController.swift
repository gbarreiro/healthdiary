//
//  BloodPressureViewController.swift
//  Health Diary
//
//  Created by Guille on 10/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import UIKit

class BloodPressureViewController: UIViewController {
    
    // MARK: UIOutlets
    @IBOutlet weak var systolicInput: UITextField!
    @IBOutlet weak var diastolicInput: UITextField!
    @IBOutlet weak var systolicAverage: UILabel!
    @IBOutlet weak var diastolicAverage: UILabel!
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var randomSwitch: UISwitch!
    
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
        // TODO: Register values 
    }
    
    // MARK: ViewController lifecycle methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
}

