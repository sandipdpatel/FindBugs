# FindBugs

### ANT Targets
- **compile**:
  * compiles test code and generates class files in _/bin_ directory 
- **test**:
  * runs junit cases from java files located in _/src_ folder and file name ending with _Test.java_.
  * generates xml report for each test class in _/report/xml_ directory
  * generates html report named _index.html_ in _/report/html_ directory

### Software requirement on build machine
- JDK 1.8.x
- ANT 1.9.x
