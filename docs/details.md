# Details for P0-Person201, Fall 2026

You should have already read the [README](../README.md) file to get an overview of the project. The details here are designed to help you complete the project once you have a general idea of what the project is about.

## Starter Code and Using Git
**_You should have installed all software (Java, Git, VS Code) before completing this project._** You can find 
the [directions for installation here](https://coursework.cs.duke.edu/201spring26/resources-201/-/blob/main/installingSoftware.md) (including workarounds for submitting without Git if needed).

We'll be using Git and the installation of GitLab at [coursework.cs.duke.edu](https://coursework.cs.duke.edu). All code for classwork will be kept here. Git is software used for version control, and GitLab is an online repository to store code in the cloud using Git.

For this project, you **start with the URL linked to course calendar**, [https://coursework.cs.duke.edu/201fall26/p0-person201](https://coursework.cs.duke.edu/201fall26/p0-person201).

**[This document details the workflow](https://coursework.cs.duke.edu/201fall26/resources-201/-/blob/main/projectWorkflow.md) for downloading the starter code for the project, updating your code on coursework using Git, and ultimately submitting to Gradescope for autograding.** We recommend that you read and follow the directions carefully this first time working on a project! While coding, we recommend that you periodically (perhaps when completing a method or small section) push your changes as explained in Section 5.


## Coding in Project P0: Person201

When you fork and clone the project, **make sure you open the correct project folder in VS Code** following the [This document details the workflow](https://coursework.cs.duke.edu/201fall26/resources-201/-/blob/main/projectWorkflow.md).

You will be working primarily with the `.java` files in the `src` folder beginning with `Person201.java`. You will modify two programs/classes (`Person201.java`,  `Person201Demo.java`), and will run `Person201Nearby.java` to see if your changes are working. Then you will create a new program called `Person201Farthest.java`.

Take a careful look at the main method you ran in `Person201Demo`. Note that it creates `Person201` **objects**. Those are defined in the `Person201.java` file using a class. When you run the program `Person201Nearby` you  can  see that it reads data about persons from a file by calling the static `read` method defined in `Person201Utilities.java`. The particular file, `data/foodlarge.txt`, contains data about some made-up people; the file is in the `data` folder of your project and you can view it directly in Visual Studio Code.

The main method you ran from `Person201Nearby` defines a `Person201` object named `query` and then searches for all other persons in the provided data file `foodlarge.txt` that are _nearby_ to the query person, and print those people to the screen. 

### Run `Person201Demo.java` and change `Person201.java`

First run the main method in `Person201Demo.java`, the output will be:

```
(037.80N,122.27W) claire, "Vin Rouge"
(001.29S,036.82E) ricardo, "Elmo's Diner"
(040.71N,073.96W) julie, "Alpaca"
names: claire, ricardo, julie
Sam to Fred distance = 424.611 
```

Make changes to `Person201.java` by creating a _default constructor_ and then changing the definition of `Person201 c` in `Person201Demo` that's currently defined as someone named **julie** to 
be `Person201 c = new Person201()` (note: _default constructor_ called). 
You must edit `Person201.java` so that with this change the output of `Person201Demo` will be as shown below.  
Note that the third line is now different since a _default constructor_ is called.

```
(037.80N,122.27W) claire, "Vin Rouge"
(001.29S,036.82E) ricardo, "Elmo's Diner"
(035.99N,078.9W) Owen, "Dain's Place"
names: claire ricardo Owen
Sam to Fred distance = 424.611
```

In a Java `class` instance variables are defined as private by convention. In this class you'll see four instance variables with identifiers `name`, `latitude`, `longitude`, and `eatery`.

In the `Person201` class, the four instance variables are named explicitly in the constructor. The use of `this.latitude` means the instance variable as opposed to `latitude` which indicates the parameter to the constructor. 

You will need to add a _default/no-parameter_ constructor to the `Person201` class. Based on the output shown above, the body of this constructor will be as shown below. The use of `this(x,y,z,w)` is an implicit call of the constructor for `Person201` that has parameters.
```
    this("Owen", 35.99, -78.9, "Dain's Place");
```


Now that you've done this, make an additional change to the `main` method in file `Person201Demo.java` by creating 
a new `Person201` object assigned to the variable named `s` as follows:

`Person201 s = new Person201("Aphe", 36.0157, -78.9183, "Monuts");`

Next, change the definition of the array `data` to include `s` such that `s` appears after `c`, e.g., 
`Person201[] data = {a,b,c,s}`. Run the program,  the output should be as shown here (note `c` still using default constructor):

```
(037.80N,122.27W) claire, "Vin Rouge"
(001.29S,036.82E) ricardo, "Elmo's Diner"
(035.99N,078.90W) Owen, "Dain's Place"
(036.02N,078.92W) Aphe, "Monuts"
names: claire, ricardo, Owen
Sam to Fred distance = 11066.524
```

You're now done editing `Person201.java` and `Person201Demo.java` !

### Running Person201Nearby and changing the Data Source

Run the program `Person201Nearby`. It reads a data file and finds all the people in the data 
file who are within 50 kilometers of a person named Ricardo who lives near Seattle. 
The program prints there are 16 people when run using the downloaded (from Git repo) file. 

Change the value of the variable `threshold` until exactly 3 people live within `threshold` 
kilometers of Ricardo. **Find the smallest `threshold` value that yields 3 people**, 
so that any value less will yield two people. You'll report on this value and how you obtained it in answering the *analysis questions* below.

Lines 16-18 in `Person201Nearby` specify three data sources: a regular text file 
named `foodlarge.txt` in the `data` folder, a `foodsmall.txt` file in the `data` folder (line 16), 
and a URL (line 18). 

How many people live within 50 kilometers of Ricardo when using the file `foodsmall` as the data source? You should run the program and be able to answer this question given a new data file as well as with the two data files provided. 

Change the code so that it reads data from the URL specified by the variable `largeURL`. Verify that you get the same results from the URL as from the file in the data folder --  since that URL references the same data as the file `foodlarge` in the `data` folder. 

### Create and Run a New Java Class: **Person201Farthest**

In the `src` folder create a new Java class named `Person201Farthest` (name the file `Person201Farthest.java`) that has only a `public static void main method` that allows the program to run. When run, the 
method should read the file `foodlarge.txt` in the `data` folder and determine the 
two distinct `Person201` objects that are the farthest apart. 
You may find the code in `Person201Nearby.java` useful in reasoning about the code you write. 
The program should print the two distinct objects that are farthest apart among all the 
objects created and returned when `PersonUtilities.readFile` is called from the code you write.

The `main` method you write *must* use the code below 
```
public static void main(String[] args) throws Exception {
        String file = "data/foodlarge.txt";
        double max = 0;
        Person201 a = null;
        Person201 b = null;
        // TODO: finish this method 
        System.out.printf("farthest distance is %3.2f between %s and %s\n",max,a.name(),b.name());
    }
```

You must write code to compare every possible pair of `Person201` objects. Typically you'll need nested loops for that unless you 
write a helper method -- in which case the loops will be nested, but the loop code in one place calls the helper function 
loop rather than seeing code like:

```
   for(int j= ....){
       for(int k= ...) {
```


**If you use DukeGPT/LLMs to help with the code**,  you should indicate that in the comments
of the new class you've written: `Person201Farthest.java`. You should also include Javadoc 
comments in your source code file that include an @author tag. See the files you're given
to help model what the comments might look like.

When you have finished, run the main method of your new `Person201Farthest` class. If everything is correct, you should two people who are farthest apart. You'll need to reason about how to determine if your resuls are correct. You can compare your results to those of other students, you can create a different data file and use that (where you know the results, for example).

### Modifying the code in `CountEateries.java`

The code you download includes a class `CountEateries` that reads a file of data, e.g., `data/foodlarge.txt` 
and determines how many `Person201` objects like each of the 40 different Ninth Street eateries (there
are 40 different eateries in the [JSON file](data/restaurants_ninth.json) in folder `data`, 
but only 32 different eateries in the file read for this program: [`data/foodlarge.txt`](data/foodlarge.txt). When run, this count is printed as zero/0 for each eatery **until you modify/add to the code in method `countEateries`** which initially returns zero for every value of parameter `eatery`. You'll need to write code to count how many `Person201` objects, say named `p`, in the array parameter `people` have `p.eatery().equals(eatery)`. Write code by looping over the array and checking every object's `.eatery()` value for equality with parameter `eatery`. Copy/paste the output you get into your **analysis questions** document.

The last several lines of eatery data should be:
```
3:	The Loop
2:	The Tavern
6:	Vin Rouge
4:	Zenfish Poke Bar
--------
total = 97
total # eateries = 32
```

## Challenge Problem and program

Using the concepts and code from `PeopleDownloder` and `Counteateries`, write a new class `Popularity` in which you copy/paste and add code so that when the program is executed it determines *at least the most chosen* ninth-street eatery accessible via the file accessed when `PeopleDownloader` is run. You can also simply download that data and write a program, but then your program won't be processing data dynamically. You can try to find the top 5 or 10 eateries rather than simply the top one. If you do this challenge problem, you'll submit the program you write as part of pushing to Git, but you should make sure there's a section in your Analysis document that describes your results and the methodology you used in writing the program. If you complete this challenge, you'll gain a great sense of satisfaction and you can earn 5 engagement points as well.

## Interlude: Understanding Multi-file Programs 

Most software consists of **many** different files, each organized into smaller units called `methods` in Java (or functions in other languages). This practice helps us to keep code organized into comprehensible units. 

For this project, `Person201.java` defines `Person201` objects (what state, or data they hold and what basic operations they support), `Person201Utilities.java` defines static methods that do things having to do with multiple `Person201` objects, and `Person201Nearby.java` uses `Person201` objects and `Person201Utilities` methods to search for nearby people given a data source. 


## Analysis Questions

Answer all the questions here. As outlined in [this document](docs/details.md) you'll submit a PDF with your answers to 
Gradescope as a separate assignment. You can use [this  .docx file](docs/p0-analysis.docx) as a template to upload to Gradescope

### Question 1 (2 points)
- See this [example dialog with DukeGPT](docs/dukegpt-static-methods.md) asking the LLM to explain something 
about static methods. Choose a Java concept that you have a question about, ask DukeGPT about the concept, and
download the dialog as a PDF which you'll include as part of the PDF you upload to Gradescope.


### Question 2 (2 points)

What is the smallest value of variable `threshold` in `Person201Nearby` that yields exactly three people near to Ricardo. 
Write a few sentences about how you found the value, essentially providing an algorithm for anyone to replicate 
your work so that with a different/new data file they could find the smallest such value **efficiently**.


### Question 3 (2 points)
The online data for this project read via URL is **not encrypted**. See
this [DukeGPT dialog about why it might be a good idea to encrypt](docs/dukegpt-peopledownloader.md) and
provide a few sentences about what makes sense and what doesn't in the answer DukeGPT provided.

### Question 4 (1 point)
According to the `.equals` method of the `Person201` class, when are two `Person201` objects considered to be equal? 
Is it case sensitive for their names or for their 
phrases? (Case sensitive means different answers are returned depending on capitalization). If you use DukeGPT or an LLM
to help answer this question, please include that dialog.

### Question 5 (1 point)

Copy/paste the output you get from modiying the method `countEateries`
in the file `CountEateries.java` based on the description above.

### Challenge
If you complete the challenge, please write up what you found. 
Answer the questions in the assignment. You'll submit your analysis as a PDF document **as a separate assignment to Gradescope**. To create a PDF, use a word processing program like Microsoft Word or Google Doc, then choose print and save-as-PDF.

***After completing the analysis questions you submit your answers in a PDF to Gradescope in the appropriate assignment.***

## Submission and Grading
You will submit the assignment on Gradescope. You can access Gradescope through the tab on Canvas. The [project workflow writeup](https://coursework.cs.duke.edu/201spring26/resources-201/-/blob/main/projectWorkflow.md) explains the how to submit your project in detail. Be sure to push changes often and be sure your final program is in your Git repository before you submit it for autograding on Gradescope. Please take note that changes/commits on GitLab are NOT automatically synced to Gradescope. You are welcome to submit as many times as you like, only the most recent submission will count for a grade.
