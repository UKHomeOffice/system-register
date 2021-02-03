import {Button} from "govuk-react";
import React from "react";

export default function SecondaryButton({children, ...rest}) {
  return (
    <Button
      type="button"
      buttonColour="#f3f2f1"
      buttonTextColour="#0b0c0c"
      {...rest}

    >
      {children}
    </Button>
  )
}