package com.olacabs.jackhammer.common;

public class ScanMailTemplate {
    public static final String emailTempalte = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "\t<head>\n" +
            "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
            "\t\t<meta name=\"viewport\" content=\"width=device-width\"/>\n" +
            "\n" +
            "\t\t<style type=\"text/css\">\n" +
            ".table {\n" +
            "\tborder-collapse: collapse !important;\n" +
            "}\n" +
            "\t.table td,\n" +
            "\t.table th {\n" +
            "\t\tbackground-color: #fff !important;\n" +
            "\t}\n" +
            "\t.table-bordered th,\n" +
            "\t.table-bordered td {\n" +
            "\t\tborder: 1px solid #ddd !important;\n" +
            "\t}\n" +
            "\thtml {\n" +
            "\t\tbox-sizing: border-box;\n" +
            "\t\tfont-family: sans-serif;\n" +
            "\t\tline-height: 1.15;\n" +
            "\t\t-webkit-text-size-adjust: 100%;\n" +
            "\t\t-ms-text-size-adjust: 100%;\n" +
            "\t\t-ms-overflow-style: scrollbar;\n" +
            "\t\t-webkit-tap-highlight-color: transparent;\n" +
            "\t}\n" +
            "*,\n" +
            "\t*::before,\n" +
            "\t*::after {\n" +
            "\t\tbox-sizing: inherit;\n" +
            "\t}\n" +
            "\n" +
            "\t@-ms-viewport {\n" +
            "\t\twidth: device-width;\n" +
            "\t}\n" +
            "\n" +
            "\tarticle, aside, dialog, figcaption, figure, footer, header, hgroup, main, nav, section {\n" +
            "\t\tdisplay: block;\n" +
            "\t}\n" +
            "\n" +
            "\tbody {\n" +
            "\t\tmargin: 0;\n" +
            "\t\tfont-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, \"Helvetica Neue\", Arial, sans-serif;\n" +
            "\t\tfont-size: 1rem;\n" +
            "\t\tfont-weight: normal;\n" +
            "\t\tline-height: 1.5;\n" +
            "\t\tcolor: #212529;\n" +
            "\t\tbackground-color: #fff;\n" +
            "\t}\n" +
            "\n" +
            "\t[tabindex=\"-1\"]:focus {\n" +
            "\t\toutline: none !important;\n" +
            "\t}\n" +
            "\n" +
            "\thr {\n" +
            "\t\tbox-sizing: content-box;\n" +
            "\t\theight: 0;\n" +
            "\t\toverflow: visible;\n" +
            "\t}\n" +
            "\n" +
            "\th1, h2, h3, h4, h5, h6 {\n" +
            "\t\tmargin-top: 0;\n" +
            "\t\tmargin-bottom: .5rem;\n" +
            "\t}\n" +
            "\n" +
            "\tp {\n" +
            "\t\tmargin-top: 0;\n" +
            "\t\tmargin-bottom: 1rem;\n" +
            "\t}\n" +
            "\n" +
            "\ttable {\n" +
            "\t\tborder-collapse: collapse;\n" +
            "\t}\n" +
            "\n" +
            "\tcaption {\n" +
            "\t\tpadding-top: 0.75rem;\n" +
            "\t\tpadding-bottom: 0.75rem;\n" +
            "\t\tcolor: #868e96;\n" +
            "\t\ttext-align: left;\n" +
            "\t\tcaption-side: bottom;\n" +
            "\t}\n" +
            "\n" +
            "\tth {\n" +
            "\t\ttext-align: left;\n" +
            "\t}\n" +
            "\n" +
            "\tlabel {\n" +
            "\t\tdisplay: inline-block;\n" +
            "\t\tmargin-bottom: .5rem;\n" +
            "\t}\n" +
            "\n" +
            "\t.table {\n" +
            "\t\twidth: 100%;\n" +
            "\t\tmax-width: 100%;\n" +
            "\t\tmargin-bottom: 1rem;\n" +
            "\t\tbackground-color: transparent;\n" +
            "\t}\n" +
            "\n" +
            "\t.table th,\n" +
            "\t.table td {\n" +
            "\t\tpadding: 0.75rem;\n" +
            "\t\tvertical-align: top;\n" +
            "\t\tborder-top: 1px solid #e9ecef;\n" +
            "\t}\n" +
            "\n" +
            "\t.table thead th {\n" +
            "\t\tvertical-align: bottom;\n" +
            "\t\tborder-bottom: 2px solid #e9ecef;\n" +
            "\t}\n" +
            "\n" +
            "\t.table tbody + tbody {\n" +
            "\t\tborder-top: 2px solid #e9ecef;\n" +
            "\t}\n" +
            "\n" +
            "\t.table .table {\n" +
            "\t\tbackground-color: #fff;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-sm th,\n" +
            "\t.table-sm td {\n" +
            "\t\tpadding: 0.3rem;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-bordered {\n" +
            "\t\tborder: 1px solid #e9ecef;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-bordered th,\n" +
            "\t.table-bordered td {\n" +
            "\t\tborder: 1px solid #e9ecef;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-bordered thead th,\n" +
            "\t.table-bordered thead td {\n" +
            "\t\tborder-bottom-width: 2px;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-striped tbody tr:nth-of-type(odd) {\n" +
            "\t\tbackground-color: rgba(0, 0, 0, 0.05);\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover tbody tr:hover {\n" +
            "\t\tbackground-color: rgba(0, 0, 0, 0.075);\n" +
            "\t}\n" +
            "\n" +
            "\t.table-primary,\n" +
            "\t.table-primary > th,\n" +
            "\t.table-primary > td {\n" +
            "\t\tbackground-color: #b8daff;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-primary:hover {\n" +
            "\t\tbackground-color: #9fcdff;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-primary:hover > td,\n" +
            "\t.table-hover .table-primary:hover > th {\n" +
            "\t\tbackground-color: #9fcdff;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-secondary,\n" +
            "\t.table-secondary > th,\n" +
            "\t.table-secondary > td {\n" +
            "\t\tbackground-color: #dddfe2;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-secondary:hover {\n" +
            "\t\tbackground-color: #cfd2d6;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-secondary:hover > td,\n" +
            "\t.table-hover .table-secondary:hover > th {\n" +
            "\t\tbackground-color: #cfd2d6;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-success,\n" +
            "\t.table-success > th,\n" +
            "\t.table-success > td {\n" +
            "\t\tbackground-color: #c3e6cb;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-success:hover {\n" +
            "\t\tbackground-color: #b1dfbb;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-success:hover > td,\n" +
            "\t.table-hover .table-success:hover > th {\n" +
            "\t\tbackground-color: #b1dfbb;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-info,\n" +
            "\t.table-info > th,\n" +
            "\t.table-info > td {\n" +
            "\t\tbackground-color: #bee5eb;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-info:hover {\n" +
            "\t\tbackground-color: #abdde5;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-info:hover > td,\n" +
            "\t.table-hover .table-info:hover > th {\n" +
            "\t\tbackground-color: #abdde5;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-warning,\n" +
            "\t.table-warning > th,\n" +
            "\t.table-warning > td {\n" +
            "\t\tbackground-color: #ffeeba;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-warning:hover {\n" +
            "\t\tbackground-color: #ffe8a1;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-warning:hover > td,\n" +
            "\t.table-hover .table-warning:hover > th {\n" +
            "\t\tbackground-color: #ffe8a1;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-danger,\n" +
            "\t.table-danger > th,\n" +
            "\t.table-danger > td {\n" +
            "\t\tbackground-color: #f5c6cb;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-danger:hover {\n" +
            "\t\tbackground-color: #f1b0b7;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-danger:hover > td,\n" +
            "\t.table-hover .table-danger:hover > th {\n" +
            "\t\tbackground-color: #f1b0b7;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-light,\n" +
            "\t.table-light > th,\n" +
            "\t.table-light > td {\n" +
            "\t\tbackground-color: #fdfdfe;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-light:hover {\n" +
            "\t\tbackground-color: #ececf6;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-light:hover > td,\n" +
            "\t.table-hover .table-light:hover > th {\n" +
            "\t\tbackground-color: #ececf6;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-dark,\n" +
            "\t.table-dark > th,\n" +
            "\t.table-dark > td {\n" +
            "\t\tbackground-color: #c6c8ca;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-dark:hover {\n" +
            "\t\tbackground-color: #b9bbbe;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-dark:hover > td,\n" +
            "\t.table-hover .table-dark:hover > th {\n" +
            "\t\tbackground-color: #b9bbbe;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-active,\n" +
            "\t.table-active > th,\n" +
            "\t.table-active > td {\n" +
            "\t\tbackground-color: rgba(0, 0, 0, 0.075);\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-active:hover {\n" +
            "\t\tbackground-color: rgba(0, 0, 0, 0.075);\n" +
            "\t}\n" +
            "\n" +
            "\t.table-hover .table-active:hover > td,\n" +
            "\t.table-hover .table-active:hover > th {\n" +
            "\t\tbackground-color: rgba(0, 0, 0, 0.075);\n" +
            "\t}\n" +
            "\n" +
            "\t.thead-inverse th {\n" +
            "\t\tcolor: #fff;\n" +
            "\t\tbackground-color: #212529;\n" +
            "\t}\n" +
            "\n" +
            "\t.thead-default th {\n" +
            "\t\tcolor: #495057;\n" +
            "\t\tbackground-color: #e9ecef;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-inverse {\n" +
            "\t\tcolor: #fff;\n" +
            "\t\tbackground-color: #212529;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-inverse th,\n" +
            "\t.table-inverse td,\n" +
            "\t.table-inverse thead th {\n" +
            "\t\tborder-color: #32383e;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-inverse.table-bordered {\n" +
            "\t\tborder: 0;\n" +
            "\t}\n" +
            "\n" +
            "\t.table-inverse.table-striped tbody tr:nth-of-type(odd) {\n" +
            "\t\tbackground-color: rgba(255, 255, 255, 0.05);\n" +
            "\t}\n" +
            "\n" +
            "\t.table-inverse.table-hover tbody tr:hover {\n" +
            "\t\tbackground-color: rgba(255, 255, 255, 0.075);\n" +
            "\t}\n" +
            "\n" +
            "\t@media (max-width: 991px) {\n" +
            "\t\t.table-responsive {\n" +
            "\t\t\tdisplay: block;\n" +
            "\t\t\twidth: 100%;\n" +
            "\t\t\toverflow-x: auto;\n" +
            "\t\t\t-ms-overflow-style: -ms-autohiding-scrollbar;\n" +
            "\t\t}\n" +
            "\t\t.table-responsive.table-bordered {\n" +
            "\t\t\tborder: 0;\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\ttr.collapse.show {\n" +
            "\t\tdisplay: table-row;\n" +
            "\t}\n" +
            "\n" +
            "\ttbody.collapse.show {\n" +
            "\t\tdisplay: table-row-group;\n" +
            "\t}\n" +
            "\n" +
            "\n" +
            "\ttable th {\n" +
            "\t\tfont-size: 0.9rem;\n" +
            "\t\tfont-weight: 400; }\n" +
            "\n" +
            "table td {\n" +
            "\tfont-size: 0.9rem;\n" +
            "\tfont-weight: 300; }\n" +
            "\n" +
            "table.table thead th {\n" +
            "\tborder-top: none; }\n" +
            "\n" +
            "table.table th,\n" +
            "table.table td {\n" +
            "\tpadding-top: 1.1rem;\n" +
            "\tpadding-bottom: 1rem; }\n" +
            "\n" +
            "table.table a {\n" +
            "\tmargin: 0;\n" +
            "\tcolor: #212529; }\n" +
            "\n" +
            "table.table .label-table {\n" +
            "\tmargin: 0;\n" +
            "\tpadding: 0;\n" +
            "\tline-height: 15px;\n" +
            "\theight: 15px; }\n" +
            "\n" +
            "table.table .btn-table {\n" +
            "\tmargin: 0px 1px;\n" +
            "\tpadding: 3px 7px; }\n" +
            "  table.table .btn-table .fa {\n" +
            "\t  font-size: 11px; }\n" +
            "\n" +
            "table.table-hover tbody tr:hover {\n" +
            "\tbackground-color: rgba(0, 0, 0, 0.075);\n" +
            "\t-webkit-transition: 0.5s;\n" +
            "\ttransition: 0.5s; }\n" +
            "\n" +
            "table .th-lg {\n" +
            "\tmin-width: 9rem; }\n" +
            "\n" +
            "table.table-sm th,\n" +
            "table.table-sm td {\n" +
            "\tpadding-top: 0.6rem;\n" +
            "\tpadding-bottom: 0.6rem; }\n" +
            "\t\t</style>\n" +
            "\t</head>\n" +
            "\t<body>\n" +
            "\t\t<p> List of vulnerabilities for {{target}} </p>\n" +
            "\t\t<table class=\"table table-striped\">\n" +
            "\n" +
            "\t\t\t<!--Table head-->\n" +
            "\t\t\t<thead>\n" +
            "\t\t\t\t<tr>\n" +
            "\t\t\t\t\t<th>Severity</th>\n" +
            "\t\t\t\t\t<th>Bug Type</th>\n" +
            "\t\t\t\t\t<th>Description</th>\n" +
            "\t\t\t\t</tr>\n" +
            "\t\t\t</thead>\n" +
            "\t\t\t<!--Table head-->\n" +
            "\n" +
            "\t\t\t<!--Table body-->\n" +
            "\t\t\t<tbody>\n" +
            "{{tbody}}" +
            "\t\t\t</tbody>\n" +
            "\t\t\t<!--Table body-->\n" +
            "\t\t</table>\n" +
            "\t\t<!--Table-->\n" +
            "\n" +
            "\t</body>\n" +
            "</html>";
    public static final String TARGET = "{{target}}";
    public static final String TBODY = "{{tbody}}";
    public static final String JACKHAMMER_SCAN_RESULTS = "Jackhammer Results";
}
