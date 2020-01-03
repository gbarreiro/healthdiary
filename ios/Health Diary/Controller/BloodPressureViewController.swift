//
//  BloodPressureViewController.swift
//  Health Diary
//
//  Created by Guille on 10/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import UIKit
import CoreData

class BloodPressureViewController: UIViewController, UITextFieldDelegate, NSFetchedResultsControllerDelegate {
    
    // Colors for the average values, depending on the risk level
    public static let riskColors: [BloodPressureReading.RiskLevel: UIColor] = [
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
    private var fetchedResultsController: NSFetchedResultsController<BloodPressureReading>!
    
    private func setupFetchedResultsController() {
        if fetchedResultsController == nil {
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

        // Registers the reading in the array
        if reading != nil{
            try? DataController.shared.viewContext.save()
        }

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
    
    // MARK: ViewController lifecycle methods
    override func viewDidLoad() {
        super.viewDidLoad()
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
    
    private func updateAverageValues() {
        // Updates the average values
        let average = BloodPressureReading.calculateAverage(records: fetchedResultsController.fetchedObjects ?? [])
        if let average = average {
            let riskColor = BloodPressureViewController.riskColors[average.riskLevel]
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
    
    
    
}

