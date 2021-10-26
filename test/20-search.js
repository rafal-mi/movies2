import fetch from 'node-fetch';
import { expect } from 'chai';

describe("Searching for the Dark Water movie playing", () => {
  it('Should return HTTP 200 OK', async () => {
    const query = encodeURIComponent(process.env.QUERY);
    const url = `${process.env.API_BASE_URL}/search/movie?query=${query}&api_key=${process.env.API_KEY}`;
    const headers = {
      "Content-Type": "application/json",
      "Accept": "application/json",
    }
    console.log(`GET ${url}`);
    let statusCode = await fetch(url, {
      method: 'GET',
      headers: headers
    }).then(async res => {
      return { statusCode: res.status, json: await res.json() }
    }).then(object => {
      console.debug(`Searching for the Dark Water movie resolved with ${JSON.stringify(object.json, null, 2)}`);
      return object.statusCode;
    }, error => {
      console.error(`Getting movies now playing rejected with ${JSON.stringify(error)}`);
    })
      .catch(err => {
        console.error(`Searching for the Dark Water movie catched with ${JSON.stringify(err)}`);
        reject(err);
      });
    expect(statusCode).to.equal(200);
  });
});
