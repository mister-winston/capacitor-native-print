import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(NativePrintPlugin)
public class NativePrintPlugin: CAPPlugin {
    private let implementation = NativePrint()
    
    @objc func print(_ call: CAPPluginCall) {
        let webView = self.webView
        
        if (webView == nil) {
            call.reject("WebView not found")
            return
        }
        
        guard let name = call.getString("name") else {
            call.reject("Name option is required")
            return
        }
        
        let printOptions = self.getPrintOptions(call)
        
        self.implementation.print(webView: webView!, name: name, printOptions: printOptions) { result in
            switch result {
            case .success(let printed):
                call.resolve([ "printed": printed ])
            case .failure(let error):
                call.reject(error.localizedDescription, "", error)
            }
        }
    }
    
    func getPrintOptions(_ call: CAPPluginCall) -> PrintOptions {
        let monochrome = call.getBool("monochrome", false)
        let orientation = {
            switch call.getString("orientation", "portrait").lowercased() {
            case "portrait":
                return UIPrintInfo.Orientation.portrait
            case "landscape":
                return UIPrintInfo.Orientation.landscape
            default:
                return UIPrintInfo.Orientation.portrait
            }
        }()
        
        return PrintOptions(monochrome: monochrome, orientation: orientation)
    }
}
