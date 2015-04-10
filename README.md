# AIMLSurgenerator
Surgenerate your AIML rules.

AIML (Artificial Intelligence Markup Language) is an XML dialect for creating natural language software agents.
It is commonly used for chatterbots.
The bot recognizes a series of pre-defined patterns and gives an answer accordingly.

For instance, the following code can result to the discussion below
```
<category>
	<pattern>weather tomorrow</pattern>
	<template>It's going to be sunny tomorrow</template>
</category>
```

```
User says: weather tomorrow
AI replies: It's going to be sunny tomorrow
```

The problematic is the following: every rule (combination of a pattern - the question - and a template - the system's answer) has to be written by hand and it becomes very hard to do something generic.
AIML allows us to use wildcards (such as *, which means "anything")

In our example, the system wouldn't recognize the sentence "what is the weather like tomorrow".
That's when wildcards become useful.

```
<category>
	<pattern>* weather * tomorrow</pattern>
	<template>It's going to be sunny tomorrow</template>
</category>
```

```
User says: what is the weather like tomorrow?
AI replies: It's going to be sunny tomorrow
```

"what is the" is the matched by the first *
and "like" is matched by the second one.

## What does this program do?
The first step is to generate all the possible wildcards for given sentences, so that you don't have to type them by hand.
The next step will be to use synonyms to extend the matching possibilities.

## How to use it?
This project can be imported in Eclipse for instance.
It takes an AIML file as input and can output it to another one.
There is also a small GUI to have a summary of the work (see SimpleTest.java)

## Example
Let's take this input AIML file (see input.aiml)

```
<category><pattern>weather tomorrow</pattern><template><srai>WEATHER</srai></template></category>
<category><pattern>weather tomorrow evening</pattern><template><srai>WEATHER</srai></template></category>
<category><pattern>weather tomorrow morning</pattern><template><srai>WEATHER</srai></template></category>
<category><pattern>weather tomorrow morning in *</pattern><template><srai>WEATHER <star/></srai></template></category>
<category><pattern>what weather tomorrow</pattern><template><srai>WEATHER</srai></template></category>
<category><pattern>what weather tomorrow in *</pattern><template><srai>WEATHER <star/></srai></template></category>
```

The program will generate 88 rules with those 6 rules by adding wildcards.

```
<category><pattern>* weather * tomorrow * evening *</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather * tomorrow * evening</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather * tomorrow * morning * in *</pattern><template><srai>WEATHER  <star index="5"/></srai></template></category>
<category><pattern>* weather * tomorrow * morning *</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather * tomorrow * morning in *</pattern><template><srai>WEATHER  <star index="4"/></srai></template></category>
<category><pattern>* weather * tomorrow * morning</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather * tomorrow *</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather * tomorrow evening *</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather * tomorrow evening</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather * tomorrow morning * in *</pattern><template><srai>WEATHER  <star index="4"/></srai></template></category>
<category><pattern>* weather * tomorrow morning *</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather * tomorrow morning in *</pattern><template><srai>WEATHER  <star index="3"/></srai></template></category>
<category><pattern>* weather * tomorrow morning</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather * tomorrow</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather tomorrow * evening *</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather tomorrow * evening</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather tomorrow * morning * in *</pattern><template><srai>WEATHER  <star index="4"/></srai></template></category>
<category><pattern>* weather tomorrow * morning *</pattern><template><srai>WEATHER </srai></template></category>
<category><pattern>* weather tomorrow * morning in *</pattern><template><srai>WEATHER  <star index="3"/></srai></template></category>
<category><pattern>* weather tomorrow * morning</pattern><template><srai>WEATHER </srai></template></category>
(...)
```

The program also allows you to *create rules* and apply them to your AIML

This is the tree representation of the given AIML file:
```
ROOT
  what
      weather
          tomorrow.
              in
                  #.                                                  
  weather
      tomorrow.
          morning.
              in
                  #.                                          
          evening. 
```

By applying the following rule, we can generate the following tree, thus creating *176 rules* this time.
(This functionality is still in beta, as you can see the word rain hasn't been cloned in the first subtree)

```
gen.applyRule("CLONE climate FROM weather");
```

```
ROOT
  what
      weather|climate
          tomorrow.
              in
                  #.                                                  
  weather|climate
      tomorrow.
          morning.
              in
                  #.                                          
          evening.  
```
