import React from "react";
import { render, screen } from "@testing-library/react";
import UpdateRiskForm from "./UpdateRiskForm";

describe("UpdateRiskForm", () => {
  const cancelHandler = jest.fn();

  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("shows informational text that includes the risk lens", () => {
    setUp({ name: "lens_name", rationale: "" });

    expect(
      screen.getByText(/enter a rationale for lens name risks/)
    ).toBeVisible();
  });

  function setUp(risk, systemName = "system") {
    return render(
      <UpdateRiskForm
        risk={risk}
        systemName={systemName}
        onCancel={cancelHandler}
      />
    );
  }
});
