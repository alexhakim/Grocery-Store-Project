# Grocery Store Android Application

## Objective

The purpose of this project is to develop a grocery store android application, linked to a barcode scanner and development kit. 

## App Features

1. Items scanned with the barcode scanner will be displayed on the app, providing a list of the user's bag.
2. Cost & Nutritional info for items
3. Login/Logout & Checkout
4. Remove items from your bag on the app 

## How to import GitHub project to Android Studio on your local device

1. On GitHub, click on Code -> Copy HTTPS link
2. Open Android Studio and go to  File -> New -> Project from Version Control
3. Make sure the Version Control is git, paste the previously copied URL and choose destination directory then click on Done
4. Wait for Android Studio to import the code (progress shown @ bottom of screen)
5. Project should now be available on Android Studio

## How to push changes from Android Studio to GitHub for the first time

##### You must first import the project from GitHub to your local device

1. Open cmd/terminal and type git --version. If you see a git version (i.e 2.33.0) then continue to next step. If not, download git from https://git-scm.com/downloads
2. Open the Grocery Store project on Android Studio
3. Go to VCS -> Enable Version Control Integration
4. Select Git as Version Control
5. On the top-left side of your screen, click on "Android" and navigate to "Project"
6. Right click on the module folder and go to Git -> Add
7. Right click on the module folder and go to Git -> Commit Directory
8. Write a description of the changes made to the project and click on Commit
9. Go to Git -> Push
10. Click on Define remote in the popup screen and set name to your username
11. Copy and paste https://github.com/alexhakim/coen390 in URL
12. You will be prompted to login to your GitHub account since you are pushing changes for the first time. If logging in with your GitHub account works, you are done. If not, follow the next steps
13. On GitHub, click on your profile and go to Settings -> Developer Settings -> Personal Access Tokens -> Generate New Token
14. Call it whatever you want, set expiration to no expiration, check the repo and notifications boxes and click on generate token
15. Paste the token link on Android Studio
 
## Team Members

1. Alexandre Hakim
2. Christopher Protopoulos
3. Anthony Salem
4. Yacin Jouiad
5. Aydin Azari Farhad
