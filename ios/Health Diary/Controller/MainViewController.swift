//
//  MainViewcont.swift
//  Health Diary
//
//  Created by Guille on 15/01/2020.
//  Copyright © 2020 Guillermo Barreiro. All rights reserved.
//

import Foundation
import UIKit
import CoreData

/**
    Common UIOutlets and methods on BloodPressureMainViewController and BodyMeasureMainViewController
 */
protocol MainViewController: UITextFieldDelegate, NSFetchedResultsControllerDelegate {
    
    // MARK: UIOutlets
    var registerButton: UIButton! { get set }
    var randomSwitch: UISwitch! { get set }
    
    // MARK: Data sources
    func setupFetchedResultsController()
    
    // MARK: User interaction

    /**
     Enables or disables the input text fields and the register button.
     - Random switch enabled: text fields disabled and register button enabled.
     - Random switch disabled: text fields enabled
        -   Both text fields filled: register button enabled
        - One or both text fields empty: register button disabled
     */
    func updateUIStatus()
    
    // MARK: Data reading and writing

    /**
     Saves on the database the introduced or random generated values.
     */
    func registerValues()
    
    /**
     Updates the average values displayed on the UI.
     
     This function is automatically called on the ViewController launch and by the NSFetchedResultsController every time a record is added or deleted.
     The records are read from the `NSFetchedResultsController`, and the average is calculated using the static method `calculateAverage()`.
     */
    func updateAverageValues()
}
