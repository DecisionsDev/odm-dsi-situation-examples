# Shipping package example

In this example, a package is shipped and delivered. The in-transit business situations must be completed within 3 days.

A `shipping package` business situation starts with the shipment of a package and ends with the delivery of the same package. The business situation expires after 3 days, which is the delivery designated time, even if the package is not shipped. The business situation can end in the expected way \(delivered\) or the timeout way \(longer than 3 days\). The business situation has an end date. At the moment of the end date, the business situation ends automatically.

The projects for the example solution can be found in the folder **[shippingpackage](../shippingpackage)**.

1.   Define the classes for the business situations. When a business situation contains a changing state, it is possible to provide a bit more information, typically, if the package is not delivered. A message, for example, can be sent to say where the package is.

```BMD
a delivery status can be one of :
    ongoing,
    delivered.
 
a shipping package is a business situation.
a shipping package has a location (a point) used as the default geometry.
a shipping package has a status (a delivery status).
```

2.   Add rules to create and identify the business situation. 

```BERL
when a package move occurs
if
    there is no shipping package  
    where this shipping package is ongoing , 
then identify a new shipping package where 
    the beginning date is now,
    the end date is 3 days after now;
```

3.   Add rules to update the business situation about the package movement and delivery. 

```BERL
when a package move occurs
definitions
  set 'shipping' to a shipping package
    where this shipping package is ongoing;
then
  set the location of shipping to the location of this package move;
```

```BERL
when a package delivered occurs
definitions 
    set 'shipping' to a shipping package
    where this shipping package is ongoing;
then
    set the status of shipping to delivered;
    end shipping;
```

4. Add a rule to notify on delivery.

```BERL
definitions 
    set 'shipping' to a shipping package
    where this shipping package is ending
    and the status of this shipping package is delivered;
then 
    emit a new outgoing package delivery event where
    the id is the id of 'the package' ,
    the duration is the duration between the beginning date of shipping and now;
```

5.   Add a rule to handle timeouts. 
Only one rule is necessary to detect that the business situation timed out without a delivery, as it happens when the business situation is ending and is not delivered.

```BERL
definitions
    set 'shipping' to a shipping package
    where this shipping package is ending
    and the status of this shipping package is not delivered;
then 
    emit a new timeout shipment where
    the id is the id of 'the package',
    the location is the location of shipping,
    the duration is the duration between the beginning date of shipping and now;
```


[![Next icon](../images/forward_32.png) **Next**](../detectingMissingSensorEvent/detectingMissing.md)

[![Back to table of contents icon](../images/home_32.png) **Back to table of contents**](../README.md)

