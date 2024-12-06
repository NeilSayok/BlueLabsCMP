package com.neilsayok.bluelabs.ui.commonUi.markdown

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.model.markdownExtendedSpans


@Composable
fun MarkdownHandler() {

    var MARKDOWN : String by remember{ mutableStateOf( """
`const` keyword is used to create immutable values, the only thing is these values needs to be defined at compile time.
😵‍💫😵‍💫😵‍💫😵‍💫😵‍💫🚀
`const` keyword has to be used with `val` keyword to create a variable. `const val` are only allowed on top level, in named objects, or in companion objects, which means it cannot be defined locally inside of functions and classes.


**Syntax for const:**


```kotlin
 const val <variable_name> = <value>
```


**Example:**


```kotlin
 const val name = "Sayok"
```


Variable defined with the `const` keyword needs to be initiated before the code is compiled, as the value stored in a `const` variable will be directly put in the source code during compilation.

To understand what is meant by the above line we need to see what happens when we use a  `const val` in Kotlin and decompile it in Java.


**Kotlin Code :**

```kotlin
object Constants {
    val name = "Sayok"
    const val petName = "Neil"
}

fun main() {
    println("Constants.name} Constants.petName}")
}
```


**Decompiled Java Code:**

```java
public final class Constants {

   public static final Constants INSTANCE = new Constants();
  
   private static final String name = "Sayok";
   
   public static final String petName = "Neil";

   public final String getName() {
      return name;
   }
}

 public static final void main() {
      System.out.println(Constants.INSTANCE.getName() + " Neil");
 }
```


So in the above code you will see the variable `petName`’s value is directly replaced in the print statement where the variable is used.


<mark>**\*Note-**</mark>`const val` only supports primitive data types as mentioned below":

| Supported data types | Supported data types |
|----|----|
| byte | byte |
| short | short |
| int | int |
| long | long |
| double | double |
| float | float |
| char | char |
| boolean | boolean |
"""
    ) }


    LazyColumn {
        item {
            Markdown(
                content = MARKDOWN.trimIndent(),
                imageTransformer = Coil3ImageTransformerImpl,
                components = markdownComponents(
                    codeBlock = highlightedCodeBlock,
                    codeFence = highlightedCodeFence,
                    //custom = tableRenderer
                    ),
                extendedSpans = markdownExtendedSpans {
                    remember {
                        ExtendedSpans(
                            RoundedCornerSpanPainter(),
                        )
                    }
                },
            )
        }
    }


}

