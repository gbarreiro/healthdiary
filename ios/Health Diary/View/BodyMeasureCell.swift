//
//  BodyMeasureCell.swift
//  Health Diary
//
//  Created by Guille on 06/01/2020.
//  Copyright © 2020 Guillermo Barreiro. All rights reserved.
//

import Foundation
import UIKit

/**
   Table cell for displaying a Body Measure record.
*/
class BodyMeasureRecordCell: UITableViewCell {
    @IBOutlet weak var weight: UILabel!
    @IBOutlet weak var height: UILabel!
    @IBOutlet weak var bmi: UILabel!
    @IBOutlet weak var recordDate: UILabel!
    @IBOutlet weak var recordHour: UILabel!
    
    /**
        Fill the cell with the data from the Body Measure reading
     */
    func loadReading(record: BodyMeasureReading){
        let riskColor = RiskColors.bmiRiskColors[record.bmiLevel]
        self.weight.text = String(format: "%.1f", record.weight) + " kg"
        self.height.text = String(record.height) + " cm"
        self.bmi.text = String(format: "%.1f", record.bmi)
        self.recordDate.text = dayFormatter.string(from: record.timestamp!)
        self.recordHour.text = hourFormatter.string(from: record.timestamp!)
        self.weight.textColor = riskColor
        self.height.textColor = riskColor
        self.bmi.textColor = riskColor
    }
    
}
