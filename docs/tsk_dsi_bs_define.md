# 2.2 How to define business situations

A business situation is defined within a rule agent and is always bound to an entity. Several business situations of the same type can be activated at the same time as each instance has a unique ID.

To define a business situation, create a class in the business model definition \(BMD\).

```businessRule
a patient staying in hospital is a business situation.
```

It can contain an ID.

```
a blocking emission is a business situation identified by a rule name.
```

It can contain any data \(concepts, events, relationships to entities\).

```
a shipping package is a business situation. 
a shipping package has a location (a point) used as the default geometry. 
a shipping package has a status (a delivery status).
```

**Note:** A business situation cannot be referenced by any other object. In other words, an entity, event, concept, or business situation cannot contain a business situation. 

[![Next icon](../images/forward_32.png) **Next**](../docs/tsk_dsi_bs_identify.md)

[![Back to table of contents icon](../images/home_32.png) **Back to table of contents**](../README.md)

