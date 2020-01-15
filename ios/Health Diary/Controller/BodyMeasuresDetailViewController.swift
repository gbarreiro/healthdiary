//
//  BodyMeasuresDetailViewController.swift
//  Health Diary
//
//  Created by Guille on 27/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import UIKit
import CoreData

/**
   Detail View Controller for Body Measure.
   Shows a list with all the stored Body Measure records, showing the weight, height and BMI values, as well as the date when it was registered.

   This View Controller reads the records from a NSFetchedResultsController, component which updates the UITableView every time a new record is added or deleted.
*/
class BodyMeasuresDetailViewController: UITableViewController, NSFetchedResultsControllerDelegate {
    
    // MARK: Data sources
    private var fetchedResultsController: NSFetchedResultsController<BodyMeasureReading>!
    
    // The NSManagedObjectContext needed for creating and deleting objects is accessed through the shared singleton in `DataController.shared.viewContext`
    
    private func setupFetchedResultsController() {
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
    
    // MARK: UIViewController lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Displays an Edit button in the navigation bar for this view controller.
        self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        // Set up FetchedResultsController
        setupFetchedResultsController()
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        // Set up FetchedResultsController
        setupFetchedResultsController()
        
        if let indexPath = tableView.indexPathForSelectedRow {
            tableView.deselectRow(at: indexPath, animated: false)
            tableView.reloadRows(at: [indexPath], with: .fade)
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        
        // Release FetchedResultsViewController
        fetchedResultsController = nil
    }
    
    @IBAction func closeViewController(_ sender: Any) {
        // Dismiss the list and goes back to the MainVC
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
        // Creates a cell and fills it with the body measure record data
        let cell = tableView.dequeueReusableCell(withIdentifier: "bodyMeasureCell", for: indexPath) as! BodyMeasureRecordCell
        let record = fetchedResultsController.object(at: indexPath)
        
        // Fills the cell with the data
        cell.loadReading(record: record)

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
