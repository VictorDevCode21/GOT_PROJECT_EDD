# Family tree.

The Hand of the King has found some striking discrepancies in the Lineage 
Register that it wishes to analyze. However, the record is written in an ancient 
and cryptic language. To help decipher it, the King has ordered the Royal 
Scribe, transcribe it in a more readable format that allows you to see the 
family trees of each lineage (surname or house). Unfortunately, the structured 
format that the Scribe knows is the JSON format, so it remains indecipherable to 
The Hand of the King. His team has been given the task of making a viewer for 
these JSON that allows The Hand of the King to navigate between the records. 
In particular, your solution must allow you, given, the name of a member of a 
certain lineage, know the record of your father or representative 
(if this exists) and what the records of your children or dependents are 
(for those who have records).


## Prerequisites.

    - Java JDK 21 or higher.
    - Netbeans IDE.
    - JSON file of stations with the format:
    {
    <Nombre de la casa>:[
        {
            <Nombre Completo1>:[
                {"Of his name":<Numeral1>},
                {"Born to":<Padre1>},
                {"Born to":<Madre1>},
                {"Known throughout as":<Mote1>},
                {"Held title":<Título nobiliario1>},
                {"Wed to":<Esposa1>},
                {"Of eyes":<Color1a>},
                {"Of hair":<Color1b>},
                {"Father to":[
                        <Nombre 1>,
                        ...
                        <Nombre n1>
                    ]
                },
                {"Notes":<Comentarios sobre su vida>},
                {"Fate":<Comentarios sobre su muerte>}
            ]
        },
        ...
        {
            <Nombre Completom>:[
                {"Of his name":<Numeralm>},
                {"Born to":<Padrem>},
                {"Born to":<Madrem>},
                {"Known throughout as":<Motem>},
                {"Held title":<Título nobiliariom>},
                {"Wed to":<Esposam>},
                {"Of eyes":<Colorma>},
                {"Of hair":<Colormb>},
                {"Father to":[
                        <Nombre 1>,
                        ...
                        <Nombre nm>
                    ]
                },
                {"Notes":<Comentarios sobre su vida>},
                {"Fate":<Comentarios sobre su muerte>}
            ]
        },

}

## Program Execution.

    1. Run the program in Netbeans IDE.
    2. Select the JSON.
    3. Select the change you want to make to the network (find person, 
    show ancestors, ect).

## Installation.

    Clone the repository in any way that suits you best:
    *HTTPS:
        https://github.com/VictorDevCode21/GOT_PROJECT_EDD.git

    *SSH:
        git@github.com:VictorDevCode21/GOT_PROJECT_EDD.git

    *Github CLI:
        gh repo clone VictorDevCode21/GOT_PROJECT_EDD

    
