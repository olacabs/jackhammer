module Openvas::V6

  # The format is given by the OMPv4 :get_reports call (OpenVASv6 uses OMPv4).
  #
  # ::OpenVAS::Result already implements v6, so basically there is nothing to
  # overwrite here. Great OO-design? Probably not!
  #
  # See:
  #   http://www.openvas.org/omp-4-0.html#command_get_reports
  class Result < ::Openvas::ReportItem
  end
end
