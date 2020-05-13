# Saleprocess

Project for the course iV1350 (Object Oriented Design) at KTH (https://www.kth.se/). 

## Output of a fake execution

```
<< Starting a new sale..
>> A new sale has started
<< Register item: 999999999
!!
!! Error! Unable to register item, please try again.
!!
<< Register item: 53
>> Added
>> ║1 pc
>> ║Amazing Fruit Cereal
>> ║63.51 SEK/pc (VAT 12.0%)
>>
>> Running total: 63.51 SEK
>>
<< Register item: 22
<< Set quantity: 5 pc
>> Added
>> ║5 pc
>> ║Luxury Fruit Drink
>> ║24.95 SEK/pc (VAT 6.0%)
>>
>> Running total: 188.26 SEK
>>
<< Register item: -1
!!
!! Error! Unable to register item: -1. Item is not found in item registry.
!!
<< Register item: 53
>> Added
>> ║1 pc
>> ║Amazing Fruit Cereal
>> ║63.51 SEK/pc (VAT 12.0%)
>>
>> Running total: 251.77 SEK
>>
<< Register item: 53
<< Set quantity: 0 pc
!!
!! Error! Unable to register item: 53. Invalid item quanitity: 0 pc.
!!
<< End sale..
>> Total cost: 251.77 SEK
<< Try discount using customer ID: 1950-05-15
>> Sale was discounted by: 36.35 SEK
>> Total cost: 215.42 SEK
<< Register paid amount: 2000.00 SEK


[PRINTING RECEIPT]
[NOTIFYING PURCHASE OBSERVERS]


╔════════════════════════════╗
║                            ║
║   Total revenue:           ║
║               215.42 SEK   ║
║                            ║
╚════════════════════════════╝
                         The Leftorium                         

              Baker Street 99, 31415 Gotham City               

Item name              Quantity      Unit price           Total
Amazing Fruit Cereal       2 pc    57.16 SEK/pc      114.32 SEK
Luxury Fruit Drink         5 pc    20.22 SEK/pc      101.10 SEK
                              ---------------------------------
Total amount                                         215.42 SEK
(Included taxes)                                      17.94 SEK

Paid amount                                         2000.00 SEK
Change returned                                     1784.58 SEK

                    2020-05-11 at 22:58:42                     


[PURCHASE OBSERVERS HAVE BEEN NOTIFIED]
[PRINTING HAS FINISHED]


>> Change to return to customer: 1784.58 SEK
<< Starting a new sale..
>> A new sale has started
<< Register item: 84
<< Set quantity: 7 pc
>> Added
>> ║7 pc
>> ║Healthy Chocolate Snack
>> ║112.35 SEK/pc (VAT 25.0%)
>>
>> Running total: 786.45 SEK
>>
<< End sale..
>> Total cost: 786.45 SEK
<< Register paid amount: 888.00 SEK


[PRINTING RECEIPT]
[NOTIFYING PURCHASE OBSERVERS]


╔════════════════════════════╗
║                            ║
║   Total revenue:           ║
║              1001.87 SEK   ║
║                            ║
╚════════════════════════════╝
                         The Leftorium                         

              Baker Street 99, 31415 Gotham City               

Item name              Quantity      Unit price           Total
Healthy Chocolate...       7 pc   112.35 SEK/pc      786.45 SEK
                              ---------------------------------
Total amount                                         786.45 SEK
(Included taxes)                                     157.29 SEK

Paid amount                                          888.00 SEK
Change returned                                      101.55 SEK

                    2020-05-11 at 22:58:42                     


[PURCHASE OBSERVERS HAVE BEEN NOTIFIED]
[PRINTING HAS FINISHED]


>> Change to return to customer: 101.55 SEK
```