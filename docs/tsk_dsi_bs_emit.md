# 2.5 How to emit events when business situations are beginning/ongoing/ending

Decisions about what to do based on the condition of the business situation are best separated into different rules.

A rule can emit other events or update an entity.

```
definitions
  set 'detection' to a missing sensor event detection
    where this missing sensor event detection is ending ;
then
  emit a new missing detected event 
    where the user is 'the user' ,
    the message is "no sensor event in the last day before " + the end date of detection ;
```

Rules can also be used to chain business situations. The following rule identifies a new business situation when a different business situation is `ending`.

```
definitions
  set 'future promotion' to a future promotion 
    where this future promotion is ending ;
then
  identify a new active promotion 
    where the beginning date is now ,
    the end date is the promotion end date of  'future promotion' ,
    the region is the region of 'future promotion' ;
```

[![Next icon](../images/forward_32.png) **Next**](../docs/tsk_dsi_bs_test.md)

[![Back to table of contents icon](../images/home_32.png) **Back to table of contents**](../README.md)

