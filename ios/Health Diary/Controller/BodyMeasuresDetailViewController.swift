//
//  BodyMeasuresDetailViewController.swift
//  Health Diary
//
//  Created by Guille on 27/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import UIKit
import CoreData

class BodyMeasuresDetailViewController: UITableViewController, NSFetchedResultsControllerDelegate {
    
    let dayFormatter: DateFormatter = DateFormatter()
    let hourFormatter: DateFormatter = DateFormatter()
    
    // MARK: Data sources
    private var fetchedResultsController: NSFetchedResultsController<BodyMeasureReading>!
    
    private func setupFetchedResultsController() {
        if fetchedResultsController == nil {
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
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Configures the date formatter
        dayFormatter.dateStyle = .medium
        dayFormatter.timeStyle = .none
        hourFormatter.dateStyle = .none
        hourFormatter.timeStyle = .short
        
        // Displays an Edit button in the navigation bar for this view controller.
        self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        setupFetchedResultsController()
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        setupFetchedResultsController()
        if let indexPath = tableView.indexPathForSelectedRow {
            tableView.deselectRow(at: indexPath, animated: false)
            tableView.reloadRows(at: [indexPath], with: .fade)
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        fetchedResultsController = nil
    }
    
    @IBAction func closeViewController(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }

    // MARK: - Table view data source
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return fetchedResultsController.sections?.count ?? 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return fetchedResultsController.sections?[section].numberOfObjects ?? 0
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "bodyMeasureCell", for: indexPath) as! BodyMeasureRecordCell
        let record = fetchedResultsController.object(at: indexPath)
        
        // Fills the cell with the data
        let riskColor = BodyMeasuresViewController.riskColors[record.bmiLevel]
        cell.weight.text = String(format: "%.1f", record.weight) + " kg"
        cell.height.text = String(record.height) + " cm"
        cell.bmi.text = String(format: "%.1f", record.bmi)
        cell.recordDate.text = dayFormatter.string(from: record.timestamp!)
        cell.recordHour.text = hourFormatter.string(from: record.timestamp!)
        cell.weight.textColor = riskColor
        cell.height.textColor = riskColor
        cell.bmi.textColor = riskColor

        return cell
    }

    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the item from the data source
            let deletedReading = fetchedResultsController.object(at: indexPath)
            DataController.shared.viewContext.delete(deletedReading)
            try? DataController.shared.viewContext.save()
        }
    }
    
    func controller(_ controller: NSFetchedResultsController<NSFetchRequestResult>, didChange anObject: Any, at indexPath: IndexPath?, for type: NSFetchedResultsChangeType, newIndexPath: IndexPath?) {
        // A reading has been added or removed
        switch type {
        case .insert:
            // Updates table
            tableView.insertRows(at: [newIndexPath!], with: .fade)
            break
        case .delete:
            // Updates table
            tableView.deleteRows(at: [indexPath!], with: .fade)
            break
        default:
            // Do nothing, not allowed ops
            break
        }
    }
    
    
    func controllerWillChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        tableView.beginUpdates()
    }
    
    func controllerDidChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        tableView.endUpdates()
    }

}


class BodyMeasureRecordCell: UITableViewCell {
    @IBOutlet weak var weight: UILabel!
    @IBOutlet weak var height: UILabel!
    @IBOutlet weak var bmi: UILabel!
    @IBOutlet weak var recordDate: UILabel!
    @IBOutlet weak var recordHour: UILabel!
    
}
