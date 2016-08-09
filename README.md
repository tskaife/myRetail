# myRetail

My first time using both gradle and spring boot went farily well.

Only requirement before running this application would be to have redis installed as well as java and gradle.

Can run the application by just running "gradle bootRun"

Can also create executable jar or war by running "gradle bootRepackage"

The resulting jar in build/libs can be run with java -jar myRetail-1.0.jar

There are really only 1 url that is mapped, but with 2 different methods
get /product/{id}
put /product/{id}

The get method responds with product information for the particular id
The put method takes a price json and updates the given id with that price

Price Json example:
{
  "value": 13.49,
  "currency_code":"USD"
}
