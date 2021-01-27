import { rest } from "msw";
import { setupServer } from "msw/node";

import ValidationError from "../validationError";
import api from '../api'
import data from '../../data/systems_dummy.json';
import SystemNotFoundException from "../systemNotFoundException";

const server = setupServer(
  rest.get("/api/systems", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json(data)
    );
  })
);

describe("api", () => {
  beforeAll(() => server.listen({ onUnhandledRequest: "error" }));

  afterAll(() => server.close());

  afterEach(() => server.resetHandlers());

  it('returns a list of systems sorted by name', async () => {
    const listOfSystems = api.getAllSystems();
    await expect(listOfSystems).resolves.toMatchObject({
      systems: [
        expect.objectContaining({
          name: "Our second system"
        }),
        expect.objectContaining({
          name: "System Register"
        })
      ]
    });
  })

  it('returns the system with the specified ID', async () => {
    const system = api.getSystem('1');
    await expect(system).resolves.toMatchObject({
      id: 1,
      name: "System Register"
    });
  })

  it('throws SystemNotFound Exeption when asked for non existant system', async () => {
    expect.assertions(2);
    try {
      await api.getSystem('9999');
    } catch (e) {
      expect(e).toBeInstanceOf(SystemNotFoundException);
      expect(e.message).toEqual('System not found');
    }
  });

  describe("update product owner", () => {
    it("sends changed owner to the API", async () => {
      server.use(
        rest.post("/api/systems/345/update-product-owner", (req, res, ctx) => {
          const { product_owner: productOwner } = req.body;
          if (productOwner !== "old owner") {
            console.error("New product owner does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateProductOwner(345, {
        productOwner: "old owner"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("removes the product owner when no value is provided", async () => {
      server.use(
        rest.post("/api/systems/234/update-product-owner", (req, res, ctx) => {
          const { product_owner: productOwner } = req.body;
          if (productOwner !== null) {
            console.error("New product owner does not match");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateProductOwner(234, {
        productOwner: ""
      });

      await expect(pendingSystem).resolves.not.toBeNull();
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/345/update-product-owner", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              productOwner: "invalid owner",
            },
          }));
        })
      );

      const pendingSystem = api.updateProductOwner(345, {
        productOwner: "$"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty(
        "errors",
        expect.objectContaining({
          productOwner: "invalid owner",
        }));
    });
  });

  describe("update technical owner", () => {
    it("sends changed owner to the API", async () => {
      server.use(
        rest.post("/api/systems/678/update-technical-owner", (req, res, ctx) => {
          const { technical_owner: technicalOwner } = req.body;
          if (technicalOwner !== "old owner") {
            console.error("New technical owner does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateTechnicalOwner(678, {
        technicalOwner: "old owner"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("removes the technical owner when no value is provided", async () => {
      server.use(
        rest.post("/api/systems/987/update-technical-owner", (req, res, ctx) => {
          const { technical_owner: technicalOwner } = req.body;
          if (technicalOwner !== null) {
            console.error("New technical owner does not match");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateTechnicalOwner(987, {
        technicalOwner: ""
      });

      await expect(pendingSystem).resolves.not.toBeNull();
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/765/update-technical-owner", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              technicalOwner: "invalid owner",
            },
          }));
        })
      );

      const pendingSystem = api.updateTechnicalOwner(765, {
        technicalOwner: "$"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty("errors", expect.objectContaining({
        technicalOwner: "invalid owner",
      }));
    });

    it("raises error if system does not exist", async () => {
      server.use(
        rest.post("/api/systems/999/update-technical-owner", (req, res, ctx) => {
          return res(ctx.status(404));
        })
      );

      const pendingSystem = api.updateTechnicalOwner(999, {
        technicalOwner: "owner",
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(SystemNotFoundException);
    });
  });

  describe("update service owner", () => {
    it("sends changed owner to the API", async () => {
      server.use(
        rest.post("/api/systems/678/update-service-owner", (req, res, ctx) => {
          const { service_owner: serviceOwner } = req.body;
          if (serviceOwner !== "old owner") {
            console.error("New service owner does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateServiceOwner(678, {
        serviceOwner: "old owner"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("removes the service owner when no value is provided", async () => {
      server.use(
        rest.post("/api/systems/987/update-service-owner", (req, res, ctx) => {
          const { service_owner: serviceOwner } = req.body;
          if (serviceOwner !== null) {
            console.error("New service owner does not match");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateServiceOwner(987, {
        serviceOwner: ""
      });

      await expect(pendingSystem).resolves.not.toBeNull();
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/765/update-service-owner", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              serviceOwner: "invalid owner",
            },
          }));
        })
      );

      const pendingSystem = api.updateServiceOwner(765, {
        serviceOwner: "$"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty("errors", expect.objectContaining({
        serviceOwner: "invalid owner",
      }));
    });

    it("raises error if system does not exist", async () => {
      server.use(
        rest.post("/api/systems/999/update-service-owner", (req, res, ctx) => {
          return res(ctx.status(404));
        })
      );

      const pendingSystem = api.updateServiceOwner(999, {
        serviceOwner: "owner",
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(SystemNotFoundException);
    });
  });

  describe("update information asset owner", () => {
    it("sends changed owner to the API", async () => {
      server.use(
        rest.post("/api/systems/678/update-information-asset-owner", (req, res, ctx) => {
          const { information_asset_owner: informationAssetOwner } = req.body;
          if (informationAssetOwner !== "old owner") {
            console.error("New information asset owner does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateInformationAssetOwner(678, {
        informationAssetOwner: "old owner"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("removes the information asset owner when no value is provided", async () => {
      server.use(
        rest.post("/api/systems/987/update-information-asset-owner", (req, res, ctx) => {
          const { information_asset_owner: informationAssetOwner } = req.body;
          if (informationAssetOwner !== null) {
            console.error("New information asset owner does not match");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateInformationAssetOwner(987, {
        informationAssetOwner: ""
      });

      await expect(pendingSystem).resolves.not.toBeNull();
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/765/update-information-asset-owner", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              informationAssetOwner: "invalid owner",
            },
          }));
        })
      );

      const pendingSystem = api.updateInformationAssetOwner(765, {
        informationAssetOwner: "$"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty("errors", expect.objectContaining({
        informationAssetOwner: "invalid owner",
      }));
    });

    it("raises error if system does not exist", async () => {
      server.use(
        rest.post("/api/systems/999/update-information-asset-owner", (req, res, ctx) => {
          return res(ctx.status(404));
        })
      );

      const pendingSystem = api.updateInformationAssetOwner(999, {
        informationAssetOwner: "owner",
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(SystemNotFoundException);
    });
  });

  describe("update business owner", () => {
    it("sends changed owner to the API", async () => {
      server.use(
        rest.post("/api/systems/678/update-business-owner", (req, res, ctx) => {
          const { business_owner: businessOwner } = req.body;
          if (businessOwner !== "old owner") {
            console.error("New business owner does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateBusinessOwner(678, {
        businessOwner: "old owner"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("removes the business owner when no value is provided", async () => {
      server.use(
        rest.post("/api/systems/987/update-business-owner", (req, res, ctx) => {
          const { business_owner: businessOwner } = req.body;
          if (businessOwner !== null) {
            console.error("New business owner does not match");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateBusinessOwner(987, {
        businessOwner: ""
      });

      await expect(pendingSystem).resolves.not.toBeNull();
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/765/update-business-owner", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              businessOwner: "invalid owner",
            },
          }));
        })
      );

      const pendingSystem = api.updateBusinessOwner(765, {
        businessOwner: "$"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty("errors", expect.objectContaining({
        businessOwner: "invalid owner",
      }));
    });

    it("raises error if system does not exist", async () => {
      server.use(
        rest.post("/api/systems/999/update-business-owner", (req, res, ctx) => {
          return res(ctx.status(404));
        })
      );

      const pendingSystem = api.updateBusinessOwner(999, {
        businessOwner: "owner",
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(SystemNotFoundException);
    });
  });

  describe("update portfolio", () => {
    it("sends changed portfolio to the API", async () => {
      server.use(
        rest.post("/api/systems/345/update-portfolio", (req, res, ctx) => {
          const { portfolio } = req.body;
          if (portfolio !== "new portfolio") {
            console.error("New portfolio does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updatePortfolio(345, {
        portfolio: "new portfolio"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });
  });

  describe("update criticality", () => {
    it("sends changed criticality to the API", async () => {
      server.use(
        rest.post("/api/systems/345/update-criticality", (req, res, ctx) => {
          const { criticality } = req.body;
          if (criticality !== "high") {
            console.error("New criticality does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateCriticality(345, {
        criticality: "high"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });
  });

  describe("update investment state", () => {
    it("sends changed investment state to the API", async () => {
      server.use(
        rest.post("/api/systems/345/update-investment-state", (req, res, ctx) => {
          const { investment_state: investmentState } = req.body;
          if (investmentState !== "sunset") {
            console.error("New investment state does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateInvestmentState(345, {
        investmentState: "sunset"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });
  });

  describe("update developed by", () => {
    it("sends updated developer to the API", async () => {
      server.use(
        rest.post("/api/systems/987/update-developed-by", (req, res, ctx) => {
          const { developed_by: developedBy } = req.body;
          if (developedBy !== "new developer") {
            console.error("Developed by value does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateDevelopedBy(987, {
        developedBy: "new developer"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/765/update-developed-by", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              developed_by: "invalid developed by value",
            },
          }));
        })
      );

      const pendingSystem = api.updateDevelopedBy(765, {
        developed_by: "$"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty(
        "errors",
        expect.objectContaining({
          developed_by: "invalid developed by value",
        })
      );
    });
  });

  describe("update supported by", () => {
    it("sends updated supporter to the API", async () => {
      server.use(
        rest.post("/api/systems/876/update-supported-by", (req, res, ctx) => {
          const { supported_by: supportedBy } = req.body;
          if (supportedBy !== "new supporter") {
            console.error("Supported by value does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateSupportedBy(876, {
        supportedBy: "new supporter"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/987/update-supported-by", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              supported_by: "invalid supported by value",
            },
          }));
        })
      );

      const pendingSystem = api.updateSupportedBy(987, {
        supported_by: "$"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty(
        "errors",
        expect.objectContaining({
          supported_by: "invalid supported by value",
        })
      );
    });
  });

  describe("update system name", () => {
    it("sends changed name to the API", async () => {
      server.use(
        rest.post("/api/systems/345/update-name", (req, res, ctx) => {
          const { name: newName } = req.body;
          if (newName !== "new name") {
            console.error("System name does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateSystemName(345, {
        name: "new name"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/345/update-name", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              name: "invalid system name",
            },
          }));
        })
      );

      const pendingSystem = api.updateSystemName(345, {
        name: "$"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty(
        "errors",
        expect.objectContaining({
          name: "invalid system name",
        }));
    });
  });

  describe("update system description", () => {
    it("sends changed description to the API", async () => {
      server.use(
        rest.post("/api/systems/456/update-system-description", (req, res, ctx) => {
          const { description } = req.body;
          if (description !== "new description") {
            console.error("New description does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateSystemDescription(456, {
        description: "new description"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("removes the description when an empty value is provided", async () => {
      server.use(
        rest.post("/api/systems/234/update-system-description", (req, res, ctx) => {
          const { description } = req.body;
          if (description !== null) {
            console.error("Expected description does not match");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateSystemDescription(234, {
        description: ""
      });

      await expect(pendingSystem).resolves.not.toBeNull();
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/345/update-system-description", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              description: "invalid description",
            },
          }));
        })
      );

      const pendingSystem = api.updateSystemDescription(345, {
        description: "x"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty(
        "errors",
        expect.objectContaining({
          description: "invalid description",
        })
      );
    });
  });
});
