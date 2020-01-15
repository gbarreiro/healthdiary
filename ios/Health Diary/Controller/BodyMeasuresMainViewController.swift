//
//  BodyMeasuresMainViewController.swift
//  Health Diary
//
//  Created by Guille on 10/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import UIKit
import CoreData

/**
   Main View Controller for Body Measures (height and weight).
   Shows the average values of the registered body measuresreadings and allows the insertion of new records.

   This View Controller reads the records from a NSFetchedResultsController, and calculates the average values every time it's notified by the NSFetchedResultsController that a new value has been inserted or deleted.

   This class acts also as a UITextFieldDelegate for avoiding the type of non-numeric characters on the text fields, and limit the number of digits.

   Through an IBAction, the text fields also trigger the `updateUIStatus()` function, in order to enable or disable the Register button.
*/
class BodyMeasuresMainViewController: UIViewController, MainViewController {    
        
    // MARK: UIOutlets
    @IBOutlet weak var weightInput: UITextField!
    @IBOutlet weak var heightInput: UITextField!
    @IBOutlet weak var weightAverage: UILabel!
    @IBOutlet weak var heightAverage: UILabel!
    @IBOutlet weak var bmiAverage: UILabel!
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var randomSwitch: UISwitch!
    
    // MARK: Data sources
    private var fetchedResultsController: NSFetchedResultsController<BodyMeasureReading>!
    
    // The NSManagedObjectContext needed for creating and deleting objects is accessed through the shared singleton in `DataController.shared.viewContext`
    
    internal func setupFetchedResultsController() {
        if fetchedResultsController == nil { // avoids creating a new NSFetchedResultsController if one already exists
            let fetchRequest:NSFetchRequest<BodyMeasureReading> = BodyMeasureReading.fetchRequest()
            let sortDescriptor = NSSortDescriptor(key: "timestamp", ascending: false) // shows the readings from newest to oldest
            fetchRequest.sortDescriptors = [sortDescriptor]
            
            fetchedResultsController = NSFetchedResultsController(fetchRequest: fetchRequest, managedObjectContext: DataController.shared.viewContext, sectionNameKeyPath: nil, cacheName: "bodymeasure")
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
            weightInput.isEnabled = false
            heightInput.isEnabled = false
            registerButton.isEnabled = true
        
        }else{
            // With the random switch off, text inputs are always enabled
            weightInput.isEnabled = true
            heightInput.isEnabled = true
            
            // With the random switch off, the register button will only be enabled if both text inputs aren't empty
            registerButton.isEnabled = (!(weightInput.text!.isEmpty) && !(heightInput.text!.isEmpty))
        }
        
    }
    
    // Avoids the typing of non-digit characters in the input text fields and of too big numbers
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        // Handle backspace/delete
        guard !string.isEmpty else {
            // Backspace detected, allow text change, no need to process the text any further
            return true
        }
        
        if textField == self.heightInput {
            // Only 3 digits allowed, no decimal points
            return string.rangeOfCharacter(from: CharacterSet.decimalDigits) != nil && textField.text!.count < 3
        } else if textField == self.weightInput {
            return string.rangeOfCharacter(from: CharacterSet.decimalDigits.union(CharacterSet(charactersIn: ".,") )) != nil && textField.text!.count < 4
        } else {
            return false
        }
        
    }

    // MARK: Data reading and writing
    
    /**
     Saves on the database the introduced or random generated body measures values.
     */
    @IBAction func registerValues() {
        var reading: BodyMeasureReading?
        
        if(randomSwitch.isOn){
            // Random values for the body measure reading
            reading = BodyMeasureReading(context: DataController.shared.viewContext, timestamp: Date())
            print("Random value\nWeight: \(String(describing: reading!.weight)). Height: \(String(describing: reading!.height))")
        } else{
            // User introduced values for the body measure reading
            if let weight = Float(weightInput.text!), let height = Int(heightInput.text!){
                if weight > 0 && height > 0 {
                    // Introduced weight and height values are positive integers
                    reading = BodyMeasureReading(context: DataController.shared.viewContext, weight: weight, height: height, timestamp: Date())
                    print("Inserted value\nWeight: \(String(describing: reading!.weight)). Height: \(String(describing: reading!.height))")
                }
            }
            
            // Clears the text inputs
            weightInput.text = ""
            heightInput.text = ""
        }
        
        // Registers the reading in the array
        if reading != nil{
            try? DataController.shared.viewContext.save()
        }

    }
    
    /**
     Updates the average body measures values displayed on the UI.
     
     This function is automatically called on the ViewController launch and by the NSFetchedResultsController every time a record is added or deleted.
     The records are read from the `NSFetchedResultsController`, and the average is calculated using the static method `BodyMeasureReading.calculateAverage()`.
     */
    internal func updateAverageValues() {
        // Updates the average values
        let average = BodyMeasureReading.calculateAverage(records: fetchedResultsController.fetchedObjects ?? [])
        if let average = average {
            let riskColor = RiskColors.bmiRiskColors[average.riskLevel]
            weightAverage.text = String(format: "%.1f", average.weight)
            heightAverage.text = String(average.height)
            bmiAverage.text = String(format: "%.1f", average.bmi)
            weightAverage.textColor = riskColor
            heightAverage.textColor = riskColor
            bmiAverage.textColor = riskColor
        } else {
            // No readings registered
            weightAverage.text = "–"
            heightAverage.text = "–"
            bmiAverage.text = "–"
            weightAverage.textColor = UIColor.purple
            heightAverage.textColor = UIColor.purple
            bmiAverage.textColor = UIColor.purple
            
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
