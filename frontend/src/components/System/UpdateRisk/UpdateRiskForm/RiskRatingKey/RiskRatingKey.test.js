import React from "react";
import { render, screen } from "@testing-library/react";
import RiskRatingKey from "./RiskRatingKey";

describe("<RiskRatingKey />", () => {
  it("displays generic description for low risk", () => {
    render(<RiskRatingKey />);

    expect(
      screen.getByText(
        /any ongoing issues will have minor or no impact on the system conducting its activities and achieving its desired goals./i
      )
    ).toBeVisible();
  });

  it("displays generic description for medium risk", async () => {
    render(<RiskRatingKey />);

    expect(
      screen.getByText(
        /any ongoing issues will have moderate impact on the system conducting its activities and achieving its desired goals, to the extent that one or more outcome objectives will fall below goals but above minimum acceptable levels./i
      )
    ).toBeVisible();
  });

  it("displays generic description for medium risk", async () => {
    render(<RiskRatingKey />);

    expect(
      screen.getByText(
        /any ongoing issues will have moderate impact on the system conducting its activities and achieving its desired goals, to the extent that one or more outcome objectives will fall below goals but above minimum acceptable levels./i
      )
    ).toBeVisible();
  });

  it("displays generic description for high risk", async () => {
    render(<RiskRatingKey />);

    expect(
      screen.getByText(
        /any ongoing issues will have a significant impact on achieving desired results, to the extent that one or more of the systems critical outcome objectives will not be achieved./i
      )
    ).toBeVisible();
  });

  it("displays generic description for unknown risk", async () => {
    render(<RiskRatingKey />);

    expect(
      screen.getByText(
        /unable to assess due to limited information available./i
      )
    ).toBeVisible();
  });

  it("displays generic description for inapplicable risk", async () => {
    render(<RiskRatingKey />);

    expect(
      screen.getByText(/this risk is not applicable to my system./i)
    ).toBeVisible();
  });
});
