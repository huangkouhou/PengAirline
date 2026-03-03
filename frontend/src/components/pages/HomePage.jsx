import { useState, useEffect } from "react";
import { useNavigate, Link } from 'react-router-dom';
import ApiService from "../../services/ApiService";
import { useMessage } from "../common/MessageDisplay";
import Select from 'react-select';

const HomePage = () => {

    const { ErrorDisplay, SuccessDisplay, showError } = useMessage();
    const navigate = useNavigate();
    const [airports, setAirports] = useState([]);

    const [searchData, setSearchData] = useState({
        departureIataCode: "",
        arrivalIataCode: "",
        departureDate: ""
    });

    const popularDestinations = [
        { id: 1, city: "New York", country: "USA", price: "$450", image: "usa.jpg" },
        { id: 2, city: "London", country: "UK", price: "$380", image: "uk.jpg" },
        { id: 3, city: "Dubai", country: "UAE", price: "$520", image: "uae.webp" },
        { id: 4, city: "Tokyo", country: "Japan", price: "$680", image: "japan.webp" }
    ];

    //Fetch all airports on component mount
    useEffect(() => {
        const fetchAirports = async () => {
            try {
                const response = await ApiService.getAllAirports();//调用 axios 去请求后端 /airports API
                setAirports(response.data || []);//把机场列表放进 React 的 state，如果null、undefined 或 false，就使用空数组 [] 作为默认值
            } catch (error) {
                showError("Failed to load airports");
            }
        };
        fetchAirports();//最后立即调用它
    },[showError]);


    const handleSearch = async(e) => {
        e.preventDefault();

        if (!searchData.departureIataCode || !searchData.arrivalIataCode){
            showError("Please select departure and arrival airports");
            return;
        }
        navigate(`/flights?departureIataCode=${searchData.departureIataCode}&arrivalIataCode=${searchData.arrivalIataCode}&departureDate=${searchData.departureDate}`);
    };

    //交换出发机场和到达机场
    const handleSwapAirports = () => {
        setSearchData({
            ...searchData,//展开原本的 searchData（保留其他字段），再交换两个值
            departureIataCode: searchData.arrivalIataCode,//重新覆盖
            arrivalIataCode: searchData.departureIataCode //重新覆盖
        });
    };

    //格式化机场选项（字符串）把某个机场对象格式化成下列格式：IATA(city)-Airport Name
    const formatAirportOption = (airport) => {
        return `${airport.iataCode}(${airport.city})-${airport.name}`;
    };

    //Convert the original airport data into the format required by react-select
    const airportOptions = airports.map(airport => ({
        value: airport.iataCode,
        label: formatAirportOption(airport)
    }));

    return (
        <div className="home-page">
            <div className="hero-section">
                <div className="hero-content">
                    <h1>Book Your Flight with Peng Airlines</h1>
                    <p>Find the best deals for your journey</p>
                </div>

                <div className="hero-box">
                    <ErrorDisplay />
                    <SuccessDisplay />
                    <form onSubmit={handleSearch}>
                        <div className="search-fields">
                            <div className="form-group">
                                <label>From</label>
                                <Select
                                    options={airportOptions}
                                    // react-select 的 value 需要传入完整的 { value, label } 对象，而不仅仅是字符串
                                    value={airportOptions.find(opt => opt.value === searchData.departureIataCode) || null}
                                    onChange={(selectedOption) => setSearchData({
                                        ...searchData,
                                        departureIataCode: selectedOption ? selectedOption.value : ""
                                    })}
                                    placeholder="Select Departure Airport"
                                    isSearchable={true} // 开启搜索功能
                                    isClearable={true}  // 允许一键清空
                                    className="react-select-container"
                                    classNamePrefix="react-select"
                                />
                            </div>

                        

                        <div className="swap-cities">
                            <button
                                type="button"
                                onClick={handleSwapAirports}
                                aria-label="Swap departure and arrival"
                            >
                                ↔
                            </button>
                        </div>

                        <div className="form-group">
                            <label>To</label>
                            <Select
                                // 过滤掉已经选为出发地的机场
                                options={airportOptions.filter(opt => opt.value !== searchData.departureIataCode)}
                                value={airportOptions.find(opt => opt.value === searchData.arrivalIataCode) || null}
                                onChange={(selectedOption) => setSearchData({
                                    ...searchData,
                                    arrivalIataCode: selectedOption ? selectedOption.value : ""
                                })}
                                placeholder="Select Arrival Airport"
                                isSearchable={true}
                                isClearable={true}
                                className="react-select-container"
                                classNamePrefix="react-select"
                            />
                        </div>

                        <div className="form-group">
                            <label>Departure Date</label>
                            <input
                                required
                                type="date"
                                value={searchData.departureDate}
                                onChange={(e) => setSearchData({
                                    ...searchData,
                                    departureDate: e.target.value
                                })}
                                min={new Date().toISOString().split('T')[0]}
                            />
                        </div>
                    </div>

                        <button type="submit" className="search-button">
                            Search Flights
                        </button>
                    </form>
                </div>
            </div>
            

            {/* Other sections like popular destinations, etc. can be added here */}
            {/* Popular Destinations */}
            <section className="popular-destinations">
                <h2>Popular Destinations</h2>
                <p>Explore our most booked flight routes</p>

                <div className="destinations-grid">
                    {popularDestinations.map(destination => (
                        <div key={destination.id} className="destination-card">
                            <div className="destination-image" style={{ backgroundImage: `url(/images/${destination.image})`}}>
                                <div className="destination-overlay">
                                    <h3>{destination.city}</h3>
                                    <p>{destination.country}</p>
                                </div>
                            </div>
                            <div className="destination-footer">
                                <span>Form {destination.price}</span>
                                <Link to="/flights" className="book-button">Book Now</Link>
                            </div>
                        </div>
                    ))}
                </div>
            </section>


            {/* why Choose Us*/}
            <section className="features-section">
                <h2>Why Choose Peng Airline</h2>
                <div className="features-grid">
                    <div className="feature-card">
                        <div className="feature-icon">✈️</div>
                        <h3>Modern Fleet</h3>
                        <p>Fly in comfort with our state-of-the-art aircraft featuring the latest amenities.</p>
                    </div>

                    <div className="feature-card">
                        <div className="feature-icon">🕒</div>
                        <h3>On-Time Performance</h3>
                        <p>We pride ourselves on our industry-leading punctuality record.</p>
                    </div>

                    <div className="feature-card">
                        <div className="feature-icon">🍽️</div>
                        <h3>Gourmet Dining</h3>
                        <p>Enjoy chef-curated meals inspired by global cuisines.</p>
                    </div>

                    <div className="feature-card">
                        <div className="feature-icon">💺</div>
                        <h3>Extra Legroom</h3>
                        <p>More space to relax with our generous seat pitch in all classes.</p>
                    </div>

                </div>
            </section>


            
            {/* Special Offers */}
            <section className="offers-section">
                <h2>Special Offers</h2>
                <p>Don't miss these exclusive deals</p>

                <div className="offer-card">
                    <div className="offer-content">
                        <h3>Summer Sale - Up to 30% Off!</h3>
                        <p>Book by June 30 for travel between July and September.</p>
                        <Link to="/flights" className="offer-button">View Deals</Link>
                    </div>
                </div>
            </section>


            {/* Testimonials */}
            <section className="Testimonials-section">
                <h2>What Our Passengers Say</h2>

                <div className="testimonials-grid">
                    <div className="testimonial-card">
                        <div className="testimonial-text">
                            "The service was exceptional from booking to landing. Will definitely fly with Peng Airline again!"
                        </div>
                        <div className="testimonial-author">
                            <div className="author-name">Sarah Johnson</div>
                            <div className="author-details">Frequent Flyer</div>
                        </div>
                    </div>

                    <div className="testimonial-card">
                        <div className="testimonial-text">
                            "Most comfortable economy seats I've experienced. The crew made the long flight enjoyable."
                        </div>
                        <div className="testimonial-author">
                            <div className="author-name">Michael Chen</div>
                            <div className="author-details">Business Traveler</div>
                        </div>
                    </div>
                </div>
            </section>
        </div>

    );

};

export default HomePage;