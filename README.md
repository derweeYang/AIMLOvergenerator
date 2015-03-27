# AIMLSurgenerator
Surgenerate your AIML rules.

## How to use
You can simply import this project in Eclipse for instance.

## What does it do?
Let's take this input AIML file

```
<category><pattern>weather tomorrow</pattern><template><srai>WEATHER</srai></template></category>
<category><pattern>weather tomorrow evening</pattern><template><srai>WEATHER</srai></template></category>
<category><pattern>weather tomorrow morning</pattern><template><srai>WEATHER</srai></template></category>
<category><pattern>what weather tomorrow</pattern><template><srai>WEATHER</srai></template></category>
<category><pattern>what weather tomorrow in *</pattern><template><srai>WEATHER <star/></srai></template></category>
```

The program will generate 121 rules will those 5 rules by adding wildcards.
```
<category><pattern>what * weather tomorrow * in *</pattern><template><srai>WEATHER<star index="3"></srai></template></category>
<category><pattern>what * weather tomorrow in *</pattern><template><srai>WEATHER<star index="2"></srai></template></category>
<category><pattern>what * weather tomorrow in *</pattern><template><srai>WEATHER<star index="2"></srai></template></category>
<category><pattern>what * weather tomorrow in *</pattern><template><srai>WEATHER<star index="2"></srai></template></category>
<category><pattern>what * weather tomorrow in *</pattern><template><srai>WEATHER<star index="2"></srai></template></category>
<category><pattern>what weather * tomorrow * in *</pattern><template><srai>WEATHER<star index="3"></srai></template></category>
<category><pattern>what weather * tomorrow * in *</pattern><template><srai>WEATHER<star index="3"></srai></template></category>
<category><pattern>what weather * tomorrow * in *</pattern><template><srai>WEATHER<star index="3"></srai></template></category>
<category><pattern>what weather * tomorrow * in *</pattern><template><srai>WEATHER<star index="3"></srai></template></category>
<category><pattern>what weather * tomorrow in *</pattern><template><srai>WEATHER<star index="2"></srai></template></category>
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
          morning|evening.
```

By applying the following rule, we can generate the following tree, thus creating 162 rules this time.
(This functionality is still in beta, as you can see the word rain hasn't been cloned in the first subtree)

```
gen.applyRule("CLONE rain FROM weather");
```

```
ROOT
  what
      weather
          tomorrow.
              in
                  #.
  weather|rain
      tomorrow.
          morning|evening.
```
