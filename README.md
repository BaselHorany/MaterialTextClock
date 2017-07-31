[![](https://jitpack.io/v/BaselHorany/MaterialTextClock.svg)](https://jitpack.io/#BaselHorany/MaterialTextClock)



# MaterialTextClock
modern TextClock allow you to customize time abbreviations (am/pm) with arabic support. you can change abbreviations color and its size ratio to time size. additionally you can change numbers format ar or en programmatically regardless of app or device languages

<p align="center">
  <img src="https://github.com/BaselHorany/MaterialTextClock/blob/master/sc.png?raw=true" width="300"/>
</p>


## Setup
1- add jitpack.io repositories to you project `build.gradle`
```java 
allprojects {
	repositories {
		...
	        maven { url 'https://jitpack.io' }
	}
}
```
2-add it as a dependency to your app `build.gradle`
```java
dependencies {
  compile 'com.github.BaselHorany:MaterialTextClock:1.0.0'
}
```
## Usage
1- in activity
```java
public class MainActivity extends AppCompatActivity {

    MaterialTextClock clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_main);

        clock = (MaterialTextClock) findViewById(R.id.clock);
        clock.setColor("#704a29");// set color for am/pm
        clock.setLang("en");//or "ar"
        clock.setSize(0.6f);// <1 or >1, represent am/pm size to clock default text size

    }
    
}
```
2- layout

```xml
    <com.basel.materialtextclock.MaterialTextClock
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/clock"
        android:textColor="@color/colorPrimary"
        android:textStyle="bold"
        android:textSize="35sp"
        android:gravity="bottom|center_horizontal"
        android:textIsSelectable="false" />
```
