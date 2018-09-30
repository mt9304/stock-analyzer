# Stock Analyzer
## Extended from [JavaFXTemplate Project](https://github.com/mt9304/javafxtemplate)

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
  - [Suggested Environment](#suggested-environment)
  - [Installation](#installation)
- [Notes](#notes)

## Introduction

This program takes a list of company stock symbols and gathers each of the company's financial data (taken from income statements, balance sheets, etc). This data is then used to calculate additional attributes such as Return on Equity and if the company is profitable. 

If a stock meets a certain criteria (such as having increasing revenue or Earnings Per Share for the past year) a respective column will be set to true and put into a database. This process automates most of the fundamental information that I look for in a stock, but a lot of manual research will still be required to do a proper analysis (for example, searching up a CEO, evaluating the business model, or watching a conference). 

## Prerequisites

### Suggested Environment

1. Java IDE with Maven installed (JDK 8)
2. JavaFX installed for IDE
3. [JavaFX SceneBuilder 8+](http://gluonhq.com/products/scene-builder/) for drag and drop UI builder (don't need if running with parameter "runWithoutGUI")
4. SQLite and [sqlitebrowser](http://sqlitebrowser.org/)

### Installation

#### For development
1. In the terminal, go to directory you want to save project in and type: 
```bash
git clone https://github.com/mt9304/stock-analyzer.git
```
2. Go into project folder and type: 
```bash
mvn install
```
3. Build/Run the Main.java file in the javafxtemplate folder

#### For exporting as a runnable jar with all dependencies included (Eclipse)
1. Make sure main class path is correct in the mainclass tag for the pom.xml, then type: 
```bash
mvn clean install assembly:single -Dmaven.test.skip=true
```
The command above skips the tests because they take a long time, but you can include the tests as well by running this instead: 
```bash
mvn clean install assembly:single
```
(Tests take a long time on purpose since a lot of them are testing the website see if the proper data is still in the expected places. This means a lot of GET requests, so I put a sleep in between each request to avoid getting black-listed)

2. After the above command completes, right click on the project and select Run As > Maven Build. Make sure you put assembly:single as a goal, then click Apply and Run
3. There should now be a jar file with dependencies in the /target folder

#### For deploying and running the jar file
1. Go to the releases section and download the latest stock-analyzer.jar
2. Make sure you have the these folders/files following this path: 
	- C:/stockdb/db
	- C:/stockdb/logs
	- C:/stockdb/tickers.txt
	
The tickers.txt file should include all the stock symboles separated by a new line with each entry. The database will be created and stored in db and logs will be in logs. These are hard-coded to be in these exact spots because I run this as part of a scheduled batch script on a remote server and it needs this structure to properly push/receive important files related to my workflow. 

3. Run this file normally without arguments to run with the JavaFX GUI. As soon as you press Start, it should start working. In v1.0.0 there are no other GUI benefits to this, but I will be working on adding charts and other visual indicators for convenience
4. Running from the command-line without GUI (this will start the scraper right away and then exit when the last ticker is completed): 
```bash
java -jar stock-analyzer.jar runWithoutGUI
```

## Notes
The scraper logic is located in the helpers.scraper package. I will be adding more data to scrape for in the future, I just have to learn more about stocks and figure out what else is important to look for in a growing company. 