
Deprecation Notice
===
Thanks to great gradle support in Android SDK and the power of AAR packaging, FastTrack project is now being
merged with [RoboTools 2](https://github.com/LivotovLabs/RoboTools). This (FastTrack) repository is now deprecated and will no longer be updated or maintained. For all future project updates please refer to [RoboTools repository](https://github.com/LivotovLabs/RoboTools) from now.





FastTrack
=========

A basic android app skeleton, completely ready to use as a basement for your every new android project. 
It also contains some general routines and base methods and classes to help you spend more time on your original
app idea and not on every-app routine tasks.




What's inside
-------------

- Pre-configured IntelliJ project, ready to open and develop.
- Pre-configured ant build script with some anchancements for automatic version name/code imprinting into manifest
- Pre-linked most commonly used library projects: 
  - ActionBarSherlock
  - ActionBar Pull To Refresh
  - ActionBar ViewPageIndicator
  - Android MenuDrawer
  - StickyListHeaders
  - GooglePlay Services

- Base activity class (extends SherlockFragmentActivity) with a set of convenient methods for:
  - Various message and input dialogs
  - Progress dialog
  - Simplified Android action bar normal and action mode menus management
  - Local (in-process only) broadcast send/receive methods

- Application class with a set of useful methods:
  - Place to put long initializing application code to run in background
  - Methods to wait for your application to initialize + pre-build SplashActivity
  - Mthods to send private broadcatsts
  
- Utility classes in .utils package:
  - UIAsyncTask to run long running tasks with extended interation with the activity and dead activity protection


Usage
-----

- Download the .zip copy of repository
- Unzip and open unzipped-folder/project in IntelliJ IDEA as a regualr project
- Use IDEA's refactoring capabilities to change package name to one you want
- Enjoy :)

Recommended way to work
-----------------------

- Make sure your activities are extending BaseActivity

- App initialization:
  - Place your long to initialize startup code into App.onInitializeAppInBackground() method to be executed in a separate thread
  - Use App.waitForApplication(...) method to wait while your app intizalization (including your code above) completes
  
- Use applicationa and contexts for any place of your code:
  - App.getContext();
  - App.getApplication();
  - App.getDialogContext() - to receive current activi activity context (needed for input/message dialogs)
  
- Ensure your activities are extending BaseActivity and presenting required information in abstract methods, you'll be forced to implement:
  - getActivityLayoutResource() - provide layout resource ID for automatic inflation or 0 to control this manually
  - getActionBarItemsMenuResource() - provide menu resource ID for an activity menu (action bar buttons)
  - getActionBarActionModeMenuResource() - provide menu resource ID for action bar ActionMode or 0 if you do not want to support action mode in this activity
  - onActionBarItemsStateUpdate() - use this method to change visivility/enabled state of your action bar menu items
  - onActionBarActionModeStarted() - called when action mode is started. Use to adjust visibility/enabled state of action mode menu items
  - onActionBarActionModeStopped() - called when action mode is ended
  - onActionBarItemSelected() - called when regular acrion bar menu item is selected. Return false to let system handle it for you.
  - onActionBarActionModeItemSelected() - called when action mode action bar item is selected. Return false to let system handle it for you.
  - use UIAsyncTask for running background tasks with feedback to UI - the progress dialog will be shown/removed automatically for you as well as error handling.  

- To control action bar action mode just call one of:
  - startActionMode() to start action mode. No other configuration requred !
  - finishActionMode() to end action mode at any time.
  - toggleActionMode() to toggle action mode at any time.
  
- To let your activity receive local broadcasts:
  - enablePrivateBroadcastsReceiver(...) to enable local broadcast listener for the specified intent filter
  - disablePrivateBroadcastsReceiver() to disable local broadcast listener for activity. Automatically disabled on activity death.
  - override onPrivateBroadcastReceived(...) method to handle received local broadcast
  - use static App.registerPrivateBroadcastReceiver(...) / App.unregisterPrivateBroadcastReceiver(...) methods to register other listeners for private broadcasts
  
- To send a local broadcast:
  - App.sendPrivateBroadcast(...) - completely async, like normal Context.sendBroadcast
  - App.sendPrivateBroadcastAndWait(...) - method will be blocked until all broadcast receivers will process request


Ant
---

Ant build script contains a custom rules file, which is automatically imported and used by ant. This adds the following features to your ant build process:

- version name and version code from version.propeties file are automatically inserted into AndroidManifest.xml on build.
- when app.debuggable property set to "true" in (version.properties), the folowing extra actions will tak place:
  - android:debuggable="true" is set to AndroidManifest.xml
  - proguard will be disabled

And once app.debuggable is removed or set to "false", android:debuggable="true" statement will be automatically removed and proguard obfuscation is enabled.



Soon
----

- A web app for preparing personalized project with your color scheme, package name, app icon and several more googies is coming soon, staytuned.
- More detailed documentation to come, this is just version 0.1 :)


