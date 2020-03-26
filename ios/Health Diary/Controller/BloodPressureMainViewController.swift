//
//  BloodPressureMainViewController.swift
//  Health Diary
//
//  Created by Guille on 10/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import UIKit
import CoreData

/**
    Main View Controller for Blood Pressure.
    Shows the average values of the registered blood pressure readings and allows the insertion of new records.
 
    This View Controller reads the records from a NSFetchedResultsController, and calculates the average values every time it's notified by the NSFetchedResultsController that a new value has been inserted or deleted.
 
    This class acts also as a UITextFieldDelegate for avoiding the type of non-numeric characters on the text fields, and limit the number of digits.
 
    Through an IBAction, the text fields also trigger the `updateUIStatus()` function, in order to enable or disable the Register button.
 */
class BloodPressureMainViewController: UIViewController, MainViewController {
    
    // MARK: UIOutlets
    @IBOutlet weak var systolicInput: UITextField!
    @IBOutlet weak var diastolicInput: UITextField!
    @IBOutlet weak var systolicAverage: UILabel!
    @IBOutlet weak var diastolicAverage: UILabel!
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var randomSwitch: UISwitch!
    
    // MARK: Data sources
    private var fetchedResultsController: NSFetchedResultsController<BloodPressureReading>!
    
    // The NSManagedObjectContext needed for creating and deleting objects is accessed through the shared singleton in `DataController.shared.viewContext`
    
    internal func setupFetchedResultsController() {
        if fetchedResultsController == nil { // avoids creating a new NSFetchedResultsController if one already exists
            let fetchRequest:NSFetchRequest<BloodPressureReading> = BloodPressureReading.fetchRequest()
            let sortDescriptor = NSSortDescriptor(key: "timestamp", ascending: false) // shows the readings from newest to oldest
            fetchRequest.sortDescriptors = [sortDescriptor]
            
            fetchedResultsController = NSFetchedResultsController(fetchRequest: fetchRequest, managedObjectContext: DataController.shared.viewContext, sectionNameKeyPath: nil, cacheName: "bloodpressure")
            
            fetchedResultsController.delegate = self
            
            do {
                try fetchedResultsController.performFetch()
            } catch {
                fatalError("The fetch could not be performed: \(error.localizedDescription)")
            }
        }

    }
    
    // MARK: User interaction
    
    /**
     Enables or disables the input text fields and the register button.
     - Random switch enabled: text fields disabled and register button enabled.
     - Random switch disabled: text fields enabled
        -   Both text fields filled: register button enabled
        - One or both text fields empty: register button disabled
     */
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
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        // Release keyboard when the user taps wherever
    self.view.endEditing(true)
    }
    
    // Avoids the typing of non-digit characters in the input text fields and of too big numbers
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        // Handle backspace/delete
        guard !string.isEmpty else {
            // Backspace detected, allow text change, no need to process the text any further
            return true
        }
        
        return string.rangeOfCharacter(from: NSCharacterSet.decimalDigits) != nil && textField.text!.count < 3
        
    }

    // MARK: Data reading and writing
    
    /**
     Saves on the database the introduced or random generated blood pressure values.
     */
    @IBAction func registerValues() {
        var reading: BloodPressureReading?
        
        if(randomSwitch.isOn){
            // Random values for the blood pressure reading
            reading = BloodPressureReading(context: DataController.shared.viewContext, timestamp: Date())
            print("Random value\nSystolic: \(String(describing: reading!.systolic)). Diastolic: \(String(describing: reading!.diastolic))")
        } else{
            // User introduced values for the blood pressure reading
            if let systolic = Int(systolicInput.text!), let diastolic = Int(diastolicInput.text!){
                if systolic > 0 && diastolic > 0 {
                    // Introduced systolic and diastolic values are positive integers
                    reading = BloodPressureReading(context: DataController.shared.viewContext, systolic: systolic, diastolic: diastolic, timestamp: Date())
                    print("Inserted value\nSystolic: \(String(describing: reading!.systolic)). Diastolic: \(String(describing: reading!.diastolic))")
                }
            }
            
            // Clears the text inputs
            systolicInput.text = ""
            diastolicInput.text = ""
        }

        // Registers the reading in the database
        if reading != nil{
            try? DataController.shared.viewContext.save()
        }

    }
    
    /**
     Updates the average blood pressure values displayed on the UI.
     
     This function is automatically called on the ViewController launch and by the NSFetchedResultsController every time a record is added or deleted.
     The records are read from the `NSFetchedResultsController`, and the average is calculated using the static method `BloodPressureReading.calculateAverage()`.
     */
    internal func updateAverageValues() {
        // Updates the average values
        let average = BloodPressureReading.calculateAverage(records: fetchedResultsController.fetchedObjects ?? [])
        if let average = average {
            let riskColor = RiskColors.bloodPressureRiskColors[average.riskLevel]
            systolicAverage.text = String(average.systolic)
            diastolicAverage.text = String(average.diastolic)
            systolicAverage.textColor = riskColor
            diastolicAverage.textColor = riskColor
        } else {
            // No readings registered
            systolicAverage.text = "–"
            diastolicAverage.text = "–"
            systolicAverage.textColor = UIColor.purple
            diastolicAverage.textColor = UIColor.purple
        }

    }
    
    func controller(_ controller: NSFetchedResultsController<NSFetchRequestResult>, didChange anObject: Any, at indexPath: IndexPath?, for type: NSFetchedResultsChangeType, newIndexPath: IndexPath?) {
        // Reading added or deleted
        updateAverageValues()

    }

    
    // MARK: ViewController lifecycle methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Set up FetchedResultsController and calculate the average values before the UI is displayed
        setupFetchedResultsController()
        updateAverageValues()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        // Set up FetchedResultsController
        setupFetchedResultsController()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        
        // Release FetchedResultsViewController
        self.fetchedResultsController = nil
    }
    
    
    
}

