SELECT
  SUM(
    le.days - 
    (SELECT SUM(ld.days) FROM leave_deduction ld WHERE le.id = ld.entitlement_id)
  ) - (
    SELECT IFNULL(SUM(ld.days),0) FROM leave_application la JOIN leave_deduction ld ON la.id = ld.application_id WHERE ld.entitlement_id IS NULL
  ) AS Balance
FROM
  leave_entitlement le
WHERE
  le.leave_type_id = 2 # ID of Annual Leave - We only consider values of the same type.
  AND le.staff_id = 1 # ID of the staff member for which we are calculating the balance
  AND le.expire_on >= '2020-08-02' # This is the date of the calculation
  AND le.entitlement_date <= '2020-08-02'; # Must be exactly the same as the above date
  
