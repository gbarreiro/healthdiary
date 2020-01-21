//
//  UITextField+Extensions.swift
//  Health Diary
//
//  Created by Guille on 15/01/2020.
//  Copyright © 2020 Guillermo Barreiro. All rights reserved.
//

import Foundation
import UIKit

/**
    Extension of UITextField for showing a Next/Register button on the number pad.
    Apple, I can't believe I have to do this by myself...
 */
extension UITextField{
    
    @IBInspectable var doneAccessory: Bool {
        get{
            return self.doneAccessory
        }

        set (hasDone) {
            if hasDone{
                addButtonOnKeyboard()
            }
        }

    }

    func addButtonOnKeyboard() {
        let buttonToolbar: UIToolbar = UIToolbar(frame: CGRect.init(x: 0, y: 0, width: UIScreen.main.bounds.width, height: 50))
        buttonToolbar.barStyle = .default

        let flexSpace = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        let button:UIBarButtonItem!
        
        if(tag==1){ // there's a text field after this one -> Next button
            button = UIBarButtonItem(title: "Next", style: .done, target: self, action: #selector(self.nextButtonAction))
        } else if(tag==2){ // last text field -> Register button
            button = UIBarButtonItem(title: "Register", style: .done, target: self, action: #selector(self.registerButtonAction))
        } else {
            return
        }

        let items = [flexSpace, button]
        buttonToolbar.items = items as? [UIBarButtonItem]
        buttonToolbar.sizeToFit()

        self.inputAccessoryView = buttonToolbar

    }

    @objc func registerButtonAction(){
        self.resignFirstResponder() // dismisses the keyboard...
        
        let tabBarController = self.superview?.window?.rootViewController as? UITabBarController
        if let viewController = tabBarController?.selectedViewController as? MainViewController {
            viewController.registerValues() // and registers the values
        }
        
    }
    
    @objc func nextButtonAction(){
        // Set next text field as responder (tag++)
        let viewController = self.superview?.window?.rootViewController // ViewController where the text field is placed
        let nextTextField = viewController?.view.viewWithTag(self.tag+1) // next TextField
        nextTextField?.becomeFirstResponder()
    }

}
