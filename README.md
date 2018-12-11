# Operational Decision Manager V8.10.0

## Decision Server Insights: Business situations \(experimental feature\)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

This repository contains a description of a new experimental concept in IBM Operational Decision Manager V8.10.0 called "business situations" and includes a set of examples. Business situations aims to address some complex event processing \(CEP\) use cases.

Decision Server Insights tracks and analyzes \(processing\) information \(data\) about things that happen \(events\), and derives a conclusion from this data. Keeping data long enough to make the right decision is a CEP dilemma.

Business situations is a concept to identify, detect, and be notified of circumstances that are of special interest. It can keep the special data for longer time periods to counteract something harmful or undesirable, or take advantage of an opportunity.

-   [1.0 What are business situations](docs/con_dsi_bs_whatisit.md)
-   **2.0 Experimenting with business situations**
	  -   [2.1 How to enable business situations in Insight Designer](docs/tsk_dsi_bs_enable.md)
      -   [2.2 How to define business situations](docs/tsk_dsi_bs_define.md)
      -   [2.3 How to identify business situations](docs/tsk_dsi_bs_identify.md)
      -   [2.4 How to update/end/abort business situations](docs/tsk_dsi_bs_update.md)
      -   [2.5 How to emit events when business situations are beginning/ongoing/ending](docs/tsk_dsi_bs_emit.md)
      -   [2.6 How to test your business situations](docs/tsk_dsi_bs_test.md)
-   **3.0 Example use cases for business situations**
      -   [3.1 Patient staying in hospital example \(event interval, regular notification\)](patientInHospital/patient.md)
      -   [3.2 Fire detection using 2 criteria example \(simple state over sliding window\)](firedetection2/firedetection.md)
      -   [3.3 Fire detection using a number of criteria example \(more complex state over sliding window\)](firedetectionmc/firedetectionmulticriteria.md)
      -   [3.4 Shipping package example \(timeout\)](shippingpackage/shippingpackage.md)
      -   [3.5 Detecting missing sensor event \(detecting absence of events\)](detectingMissingSensorEvent/detectingMissing.md.md)
      -   [3.6 Promotion \(a situation that follows a situation\)](promotion/tsk_dsi_bs_example_multiple_situations.md)


# Issues and contributions

For issues relating to IBM Operational Decision Manager you can [get help](https://developer.ibm.com/odm/home/connect/) through the ODMDev community or, if you have production licenses for Operational Decision Manager, via the usual support channels. We welcome contributions following [our guidelines](CONTRIBUTING.md).


<p align="center">
  <a href="https://join.slack.com/t/odmdev/shared_invite/enQtMjU0NzIwMjM1MTg0LTQyYTMzNGQ4NzJkMDYxMDU5MDRmYTM4MjcxN2RiMzNmZWZmY2UzYzRhMjk0N2FmZjU2YzJlMTRmN2FhZDY4NmQ">
        Follow us on slack
        <br>
        <img src="https://a.slack-edge.com/436da/marketing/img/meta/favicon-32.png">
  </a>
</p>

# License
[Apache 2.0](LICENSE)

# Notice
© Copyright IBM Corporation 2018.

Icons from https://github.com/IBM-Design/icons licensed under [Creative Commons Attribution 4.0 International License](http://creativecommons.org/licenses/by/4.0/)
