import React from "react";
import { render, screen } from "@testing-library/react";

import UpdateContacts from "../UpdateContacts";

describe("UpdateContacts", () => {
  it("displays the name of the system", () => {
    render(<UpdateContacts system={{ name: 'system name' }} />);

    expect(screen.getByRole("heading")).toHaveTextContent("system name");
  });

  it("displays a loading message if data is unavailable", () => {
    render(<UpdateContacts system={null} />);

    expect(screen.getByText(/loading system data/i)).toBeInTheDocument();
  });
});
