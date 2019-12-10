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
    
    // MARK: UIActions
    @IBAction func randomSwitchToggled(_ sender: UISwitch) {
        // TODO: Enable or disable the input UITextFields
    }
    
    @IBAction func registerValues() {
    }
    
    // MARK: ViewController lifecycle methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    @IBAction func inputTextChanged() {
        // TODO: Enable or disable the input UITextFields
    }
    
}

