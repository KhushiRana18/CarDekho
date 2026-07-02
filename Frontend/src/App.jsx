import { useState } from 'react';
import './App.css';

const initialForm = {
  budget: '1200000',
  fuel: 'Petrol',
  transmission: 'Automatic',
  bodyType: 'SUV',
  prompt: 'Need a reliable automatic SUV with good mileage.'
};

function App() {
  const [form, setForm] = useState(initialForm);
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [expanded, setExpanded] = useState({});

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch('/recommend', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });

      if (!response.ok) {
        throw new Error('Unable to fetch recommendations');
      }

      const data = await response.json();
      setRecommendations(data.recommendations || []);
      setExpanded({});
    } catch (err) {
      setError(err.message || 'Something went wrong');
    } finally {
      setLoading(false);
    }
  };

  const toggleExpand = (id) => {
    setExpanded((prev) => ({ ...prev, [id]: !prev[id] }));
  };

  const formatScore = (score) => (score ? (score * 100).toFixed(0) + '%' : '—');

  return (
    <div className="app-shell">
      <header className="hero-card">
        <h1>CarDekho Recommendation Engine</h1>
        <p>Describe your needs and get a shortlist of cars ranked by budget, mileage, safety, and reliability.</p>
      </header>

      <form className="form-card" onSubmit={handleSubmit}>
        <div className="field-grid">
          <label>
            Budget
            <input name="budget" type="number" value={form.budget} onChange={handleChange} />
          </label>
          <label>
            Fuel
            <select name="fuel" value={form.fuel} onChange={handleChange}>
              <option value="Petrol">Petrol</option>
              <option value="Diesel">Diesel</option>
              <option value="Electric">Electric</option>
            </select>
          </label>
          <label>
            Transmission
            <select name="transmission" value={form.transmission} onChange={handleChange}>
              <option value="Automatic">Automatic</option>
              <option value="Manual">Manual</option>
            </select>
          </label>
          <label>
            Body Type
            <select name="bodyType" value={form.bodyType} onChange={handleChange}>
              <option value="SUV">SUV</option>
              <option value="Sedan">Sedan</option>
              <option value="Hatchback">Hatchback</option>
            </select>
          </label>
        </div>
        <label>
          Prompt
          <textarea name="prompt" rows="3" value={form.prompt} onChange={handleChange} />
        </label>
        <button type="submit" disabled={loading}>{loading ? 'Finding cars...' : 'Get Recommendations'}</button>
      </form>

      {error ? <p className="status error">{error}</p> : null}

      <section className="results-section">
        <h2>Recommendations</h2>
        {recommendations.length === 0 && !loading ? (
          <p className="status">Submit the form to see your shortlist.</p>
        ) : null}

        <div className="card-list">
          {recommendations.map((car) => (
            <article key={car.id} className="recommendation-card">
              <div className="card-header">
                <div>
                  <h3>{car.brand} {car.model}</h3>
                  <p className="car-meta">{car.year || car.registrationYear} • {car.ownership} owner</p>
                </div>
                <span className="score-pill">{formatScore(car.matchScore)}</span>
              </div>

              <div className="specs-grid">
                <div className="spec">
                  <span className="spec-label">Price</span>
                  <span className="spec-value">₹{car.price?.toLocaleString() || '—'}</span>
                </div>
                <div className="spec">
                  <span className="spec-label">Mileage</span>
                  <span className="spec-value">{car.mileage?.toFixed(1) || '—'} kmpl</span>
                </div>
                <div className="spec">
                  <span className="spec-label">Power</span>
                  <span className="spec-value">{car.power?.toFixed(0) || '—'} bhp</span>
                </div>
                <div className="spec">
                  <span className="spec-label">Boot Space</span>
                  <span className="spec-value">{car.bootSpace?.toFixed(0) || '—'} L</span>
                </div>
                <div className="spec">
                  <span className="spec-label">Safety</span>
                  <span className="spec-value">{car.safetyScore?.toFixed(1) || '—'}/10</span>
                </div>
                <div className="spec">
                  <span className="spec-label">Seats</span>
                  <span className="spec-value">{car.seats || '—'}</span>
                </div>
              </div>

              <div className="explanation-section">
                <div className="explanation-header">
                  <h4>Why This Car?</h4>
                </div>
                {car.matchReasons && car.matchReasons.length > 0 && (
                  <ul className="match-reasons">
                    {car.matchReasons.map((reason) => (
                      <li key={reason}>{reason}</li>
                    ))}
                  </ul>
                )}
              </div>

              <div className="pro-con-section">
                <div className="pro-con-block">
                  <h4>✓ Advantages</h4>
                  <ul>
                    {car.pros?.map((item) => (
                      <li key={item}>{item}</li>
                    ))}
                  </ul>
                </div>
                <div className="pro-con-block">
                  <h4>⚠ Considerations</h4>
                  <ul>
                    {car.cons?.map((item) => (
                      <li key={item}>{item}</li>
                    ))}
                  </ul>
                </div>
              </div>

              <button
                type="button"
                className="expand-btn"
                onClick={() => toggleExpand(car.id)}
              >
                {expanded[car.id] ? 'Hide Details' : 'Show Full Details'}
              </button>

              {expanded[car.id] && (
                <div className="expanded-details">
                  <div className="detail-section">
                    <h5>Scoring Breakdown</h5>
                    <div className="score-breakdown">
                      {car.scoreBreakdown && Object.entries(car.scoreBreakdown).map(([key, value]) => (
                        <div key={key} className="score-item">
                          <span className="score-key">{key}</span>
                          <div className="score-bar">
                            <div className="score-fill" style={{ width: `${Math.min(value * 100, 100)}%` }}></div>
                          </div>
                          <span className="score-num">{(value * 100).toFixed(1)}%</span>
                        </div>
                      ))}
                    </div>
                  </div>

                  <div className="detail-section">
                    <h5>Complete Specs</h5>
                    <div className="detail-grid">
                      <div><strong>Fuel:</strong> {car.fuel || '—'}</div>
                      <div><strong>Transmission:</strong> {car.transmission || '—'}</div>
                      <div><strong>Body Type:</strong> {car.bodyType || '—'}</div>
                      <div><strong>Engine:</strong> {car.engine?.toFixed(2) || '—'} cc</div>
                      <div><strong>City:</strong> {car.city || '—'}</div>
                      <div><strong>KM Driven:</strong> {car.kilometersDriven?.toLocaleString() || '—'} km</div>
                      <div><strong>Color:</strong> {car.color || '—'}</div>
                      <div><strong>Maintenance Score:</strong> {car.maintenanceScore?.toFixed(1) || '—'}/10</div>
                      <div><strong>Reliability Score:</strong> {car.reliabilityScore?.toFixed(1) || '—'}/10</div>
                    </div>
                  </div>

                  <div className="detail-section">
                    <h5>Scores</h5>
                    <div className="detail-grid">
                      <div><strong>TOPSIS Score:</strong> {(car.topsisScore * 100).toFixed(1)}%</div>
                      <div><strong>Semantic Match:</strong> {(car.semanticScore * 100).toFixed(1)}%</div>
                      <div><strong>Final Score:</strong> {(car.matchScore * 100).toFixed(1)}%</div>
                    </div>
                  </div>
                </div>
              )}
            </article>
          ))}
        </div>
      </section>
    </div>
  );
}

export default App;
