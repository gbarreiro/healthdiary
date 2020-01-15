//
//  Data Controller.swift
//  Health Diary
//
//  Created by Guille on 02/01/2020.
//  Copyright © 2020 Guillermo Barreiro. All rights reserved.
//

import Foundation
import CoreData

/**
    Class for managing all the operations related with persistance of data, using the CoreData framework.
    The singleton `shared` must be loaded from the `AppDelegate`.
 */
class DataController {
    
    private static let MODEL_NAME = "HealthModel"
    
    public static var shared: DataController = DataController(modelName: DataController.MODEL_NAME)// singleton
    
    let persistentContainer:NSPersistentContainer
    
    var viewContext:NSManagedObjectContext {
        return persistentContainer.viewContext
    }
    
    let backgroundContext:NSManagedObjectContext!
    
    private init(modelName:String) {
        persistentContainer = NSPersistentContainer(name: modelName)
        
        backgroundContext = persistentContainer.newBackgroundContext()
    }
    
    func configureContexts() {
        viewContext.automaticallyMergesChangesFromParent = true
        backgroundContext.automaticallyMergesChangesFromParent = true
        
        backgroundContext.mergePolicy = NSMergePolicy.mergeByPropertyObjectTrump
        viewContext.mergePolicy = NSMergePolicy.mergeByPropertyStoreTrump
    }
    
    func load(completion: (() -> Void)? = nil) {
        persistentContainer.loadPersistentStores { storeDescription, error in
            guard error == nil else {
                fatalError(error!.localizedDescription)
            }
            self.configureContexts()
            completion?()
        }
    }
}
