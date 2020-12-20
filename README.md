# Sudoku Game

[toc]

************

## Introduce

* A conventional sudoku game with 9*9 blocks using Java and through Android Studio.
* As for the specific operation rule of this game , you need to first press the blocks above the 9*9 matrix blocks, and drug it to the '?' or the block you want to change (not including the original array) .
* click the "SHOWANSWER"  button , the complete correct Sudoku matrix will show on the screen ,with the imgs on the unknown blocks bling twice

### Preview

### ![](https://s401177923-1302493622.cos.ap-nanjing.myqcloud.com/mdImages/mainScreen.png) ![levelSelect](https://s401177923-1302493622.cos.ap-nanjing.myqcloud.com/mdImages/levelSelect.png)![](https://s401177923-1302493622.cos.ap-nanjing.myqcloud.com/mdImages/gameScreen.png)![answerShow](https://s401177923-1302493622.cos.ap-nanjing.myqcloud.com/mdImages/answerShow.png)![records](https://s401177923-1302493622.cos.ap-nanjing.myqcloud.com/mdImages/records.png)![record_2](https://s401177923-1302493622.cos.ap-nanjing.myqcloud.com/mdImages/record_2.png)![record_3](https://s401177923-1302493622.cos.ap-nanjing.myqcloud.com/mdImages/record_3.png)

## UI Settings

### 	Previous  Thought（avoid delay）

![image-20201220105134759](https://s401177923-1302493622.cos.ap-nanjing.myqcloud.com/mdImages/image-20201220105134759.png)

* 9*9 Blocks Design  :  It's certain to abandon statically add ImageView to the parent view. So First I was thinking to use GridLayout, and dynamically generate  81 ImageViews inside it . It worked  , but hearing that recyclerview can save some storage using ..?(not so sure),so I turn my side to Recyclerview.But it proves that in a way it's actually better ,at least I don't need to care about the dividing line ...  
* Game-Run Rules  :  In the  first , the action that moving finger on screen to dynamically set the block position of choosing number may be a delayed process ( After that , it proves that I was wrong . Delay is just the images is too big...). So I choose to a rule when click a number above  , and click again the blocks below ,to fill the 9x9 blocks . But this method is just a conventional way ,not attractive enough

### 	Better Way (Improve User Experience)

* So after I ran through the delayed problem and adjusted the imgs to a much smaller size.(from 800 kb to 4kb per Images....) , I go back to my first strategy, to timely let the block move with your finger. And when your finger get up ,it will be removed from parent views ,and there will be a judge about its position. 

### 	Thoughts of Afterclass Improvement

* maybe some ugly button imgs can be changed?
* packages classification
* MVC model 

## Using of Class

![image-20201219223339691](https://s401177923-1302493622.cos.ap-nanjing.myqcloud.com/mdImages/image-20201219223339691.png)

* MainActivity:  handling LegacyRequest   , LitePal Connect and some Ui settings

* AboutActivity: developer information  and Lottie Animation

* RecordBitmap: a bean class with some necessary data of a complete game record , including view bitmaps, dates ,completion date.....

* LevelChoosePanel: a Ui class for Level-Choose-Panel Ui layout

* WrongBorder : a Ui class for a  border view when detecting answer is wrong.

* RecordShowActivity: handling Records show page , including a recyclerview layout .

* recycleutil:  Recyclerview Adapter and Decoration for its Ui setting.

* **GamingActivity:**  most important activity in this project. including  handling logically generate 9*9 Sudoku matrix, dynamically generate 9x9Ui Blocks(ImageView actually) and check wrong blocks in real time. Also handling gesture listener including finger DOWN , MOVE and UP .  

* **SudoHelper** : core algorithm handling module class , to generate a 9*9 Sudoku with only one answer ,and detect validity of current 9x9 Matrix

  

## Algorithm Use

### 	Previous Thoughts And Problem :

* Previously , I was thinking to use a way of generation method with not-only answer. The method is quite easy. First to choose N (N=30) blocks , each row , column and numbers inside it is random on the condition that it matches the rules of Sudoku. And them use backTrace Method to fill other blocks remaining. And when to judge in the gameActivity , we just need to use the **isValid()** method to detect if this column & row & 3*3 matrix around it fit the rules , if not , that's wrong. Thus it may provide different answers, due to detecting the 9x9 matrix it provides in the first step.

### 	After Improvement :

* As seeing more, a standard Sudoku is a 9*9 Matrix with only one answer. So , to improve this , I use two types of buckTrace() method , difference is if it changes the origin matrix it gives. 

* So , first We randomly choose N different block , and under the rules, filling it with random number. Then using the original backTrace()  method (changes params it gives in). We get a correct 9x9 Sudoku Matrix. And then, we dig put specific numbers of blocks . In each digging process ,we detect that besides the original number in the matrix ,if there's other number that could make current matrix be a standard matrix . If not , we can safely dig out this number ,and go further , else ,we may need to change the position （generate row and column randomly again）,until we dig out enough numbers of blocks . We get a matrix with only one answer.

* Some optimization thoughts: if some time , the matrix is not good. Maybe we randomly find many blocks ,but it still can't be dug out .Therefore ,we may need to go back in the first step, generating a new matrix and do the works below. So we can set a number to record the failed dig-out search , if the number is bigger than a certain number , we re-generate it . So ,in some case ,it may avoid time consuming work inside this methods.

  

## Things can be better

* Ui , have to say , the Ui design is really so so...But time is catching up , no time for that . So sometime Ui need to be modified.

## Thanks : 

* Sincerely thanks for watching this. If any details and humble opinion could contribute to your thoughts , I would be content. And if you have any advice or doubts , welcome to put it there.

* Contact : 401177923@qq.com  



